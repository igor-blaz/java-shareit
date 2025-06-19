package ru.practicum.shareit.request;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RequestServiceTest {
    @Autowired
    private ItemRequestService requestService;

    @MockBean
    private ItemRequestStorage itemRequestStorage;

    @MockBean
    private UserService userService;

    private ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        ItemRequestDto itemRequestDto = Instancio.create(ItemRequestDto.class);
        itemRequest = Instancio.create(ItemRequest.class);
    }

    @Test
    void saveRequest() {
        when(itemRequestStorage.saveItemRequest(any(ItemRequest.class))).thenReturn(itemRequest);
        when(userService.getUser(anyLong())).thenReturn(itemRequest.getRequestor());
        ItemRequestDto actualItemRequest = requestService.saveRequest(itemRequest, itemRequest.getRequestor().getId());

        assertThat(actualItemRequest).isNotNull();
        assertThat(actualItemRequest.getDescription()).isEqualTo(itemRequest.getDescription());

        verify(itemRequestStorage, times(1)).saveItemRequest(any(ItemRequest.class));
    }

    @Test
    void getAllNotMineTest() {
        Long userId = 1L;
        List<ItemRequest> expected = Instancio.ofList(ItemRequest.class).size(3).create();

        when(itemRequestStorage.getAllNotMine(userId)).thenReturn(expected);

        List<ItemRequest> actual = requestService.getAllNotMine(userId);

        assertThat(actual).isEqualTo(expected);
        verify(itemRequestStorage).getAllNotMine(userId);
    }

    @Test
    void getRequestsByUserIdTest() {
        Long userId = 1L;
        User user = Instancio.create(User.class);
        user.setId(userId);

        List<ItemRequest> requestList = Instancio.ofList(ItemRequest.class).size(2).create();

        when(userService.getUser(userId)).thenReturn(user);
        when(itemRequestStorage.getItemRequestByUserId(userId)).thenReturn(requestList);

        List<ItemRequest> result = requestService.getRequestsByUserId(userId);

        assertThat(result).isEqualTo(requestList);
        verify(userService).getUser(userId);
        verify(itemRequestStorage).getItemRequestByUserId(userId);
    }


}
