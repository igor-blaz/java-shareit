package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

//@Data
//@RequiredArgsConstructor
//@AllArgsConstructor
//public class BookingDto {
//    private Long id;
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
//    private LocalDateTime start;
//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
//    private LocalDateTime end;
//    private BookingStatus status;
//    private Long itemId;
//    private Long bookerId;
//
//
//    private BookerShortDto booker;
//    private ItemShortDto item;
//}

@Data
@RequiredArgsConstructor
public class BookingDto {
    private final Long id;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime start;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime end;
    private final Booker booker;
    private final Item item;
    private final BookingStatus status;

    @Data
    public static class Booker {
        private final long id;
        private final String name;
    }

    @Data
    public static class Item {
        private final long id;
        private final String name;
    }
}