package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
public class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    ItemRequestDto itemRequestDto;
    ItemRequest itemRequest;

    @BeforeEach
    void setUp() {
        itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(1L);
        itemRequestDto.setDescription("Нужен шуруповерт");
        itemRequestDto.setCreated(LocalDateTime.now());
        itemRequestDto.setRequestorId(1L);

        itemRequest = RequestMapper.createItemRequest(itemRequestDto);
        itemRequest.setRequestor(new User(100L, "Igor", "igor@gmail.com"));
    }

    @Test
    void addItemRequestTest() throws Exception {
        when(itemRequestService.saveRequest(any(ItemRequest.class), anyLong()))
                .thenReturn(itemRequestDto);
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()));

        verify(itemRequestService, times(1))
                .saveRequest(any(ItemRequest.class), eq(1L));
    }

    @Test
    void getItemRequestByIdTest() throws Exception {
        when(itemRequestService.getByRequestId(anyLong())).thenReturn(itemRequestDto);
        mockMvc.perform(get("/requests/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(itemRequestDto.getId()))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()));

        verify(itemRequestService, times(1))
                .getByRequestId(1L);
    }


    @Test
    void getAllNotMineTest() throws Exception {
        ItemRequestDto expectedDto = RequestMapper.createDto(itemRequest);

        when(itemRequestService.getAllNotMine(anyLong()))
                .thenReturn(List.of(itemRequest));

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 2L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(expectedDto.getId()))
                .andExpect(jsonPath("$[0].description").value(expectedDto.getDescription()));

        verify(itemRequestService, times(1)).getAllNotMine(2L);
    }
}
