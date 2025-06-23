package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestStorageTest {

    @Mock
    private RequestRepository requestRepository;

    @InjectMocks
    private ItemRequestStorage itemRequestStorage;

    @Test
    void saveItemRequestTest() {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);

        when(requestRepository.save(itemRequest)).thenReturn(itemRequest);

        ItemRequest saved = itemRequestStorage.saveItemRequest(itemRequest);

        assertEquals(itemRequest, saved);
        verify(requestRepository).save(itemRequest);
    }

    @Test
    void getItemRequestByUserIdTest() {
        Long userId = 1L;
        List<ItemRequest> list = List.of(new ItemRequest(), new ItemRequest());

        when(requestRepository.findAllByRequestorIdOrderByCreatedDesc(userId)).thenReturn(list);

        List<ItemRequest> result = itemRequestStorage.getItemRequestByUserId(userId);

        assertEquals(list, result);
    }

    @Test
    void getByRequestIdTest() {
        Long id = 1L;
        ItemRequest request = new ItemRequest();
        request.setId(id);

        when(requestRepository.findById(id)).thenReturn(Optional.of(request));

        ItemRequest found = itemRequestStorage.getByRequestId(id);

        assertEquals(request, found);
    }

    @Test
    void getByRequestIdErrorTest() {
        Long id = 2L;

        when(requestRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            itemRequestStorage.getByRequestId(id);
        });

        assertTrue(exception.getMessage().contains("запроса не существует"));
    }

    @Test
    void getAllNotMineTest() {
        Long userId = 1L;
        List<ItemRequest> list = List.of(new ItemRequest(), new ItemRequest());

        when(requestRepository.findAllByRequestorIdNot(userId)).thenReturn(list);

        List<ItemRequest> result = itemRequestStorage.getAllNotMine(userId);

        assertEquals(list, result);
    }
}

