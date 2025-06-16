package ru.practicum.shareit.booking;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemRequestDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookingServiceImpl bookingService;

    private BookingDto bookingDto;
    private BookingItemRequestDto bookingRequestDto;

    @BeforeEach
    void setUp() {
        bookingRequestDto = new BookingItemRequestDto(1L,
                LocalDateTime.of(2025, 6, 20, 12, 0),
                LocalDateTime.of(2025, 6, 21, 12, 0));

        bookingDto = new BookingDto(
                10L,
                bookingRequestDto.getStart(),
                bookingRequestDto.getEnd(),
                new BookingDto.Booker(1L, "Igor"),
                new BookingDto.Item(2L, "ItemName"),
                BookingStatus.APPROVED
        );
    }

    @Test
    void addBookingTest() throws Exception {
        when(bookingService.addBooking(any(), eq(1L))).thenReturn(bookingDto);

        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookingRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.status").value(bookingDto.getStatus().toString()));
    }

    @Test
    void getAllUserBookingsTest() throws Exception {
        when(bookingService.getBookingDtoByUserId(1L, BookingState.ALL))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()));
    }

    @Test
    void getBookingByBookingIdTest() throws Exception {
        when(bookingService.getBookingDto(10L, 1L)).thenReturn(bookingDto);

        mockMvc.perform(get("/bookings/10")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()));
    }

    @Test
    void getAllBookingByUserIdTest() throws Exception {
        when(bookingService.getBookingDtoByUserId(1L, BookingState.ALL)).thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(bookingDto.getId()));
    }

    @Test
    void patchBookingStatusTest() throws Exception {
        when(bookingService.updateBookingStatus(10L, true, 1L)).thenReturn(bookingDto);

        mockMvc.perform(patch("/bookings/10")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookingDto.getId()))
                .andExpect(jsonPath("$.status").value(bookingDto.getStatus().toString()));
    }
}
