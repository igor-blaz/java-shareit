package ru.practicum.shareit.booking.dto;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.dto.ItemMapper;

import java.util.List;


@Slf4j
@UtilityClass
public class BookingMapper {
    public static BookingDto modelToDto(Booking booking) {
        log.info("Маппинг Booking ->{}", booking);
        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setStatus(booking.getBookingStatus());
        bookingDto.setItemId(booking.getItemId());
        bookingDto.setBookerId(booking.getBookerId());
        bookingDto.setBooker((createShortDto(booking)));
        bookingDto.setItem(ItemMapper.createShortDto(booking.getItem()));
        log.info("Маппинг BookingDTO ->{}", bookingDto);
        return bookingDto;
    }

    public static BookerShortDto createShortDto(Booking booking) {
        return new BookerShortDto(booking.getBookerId());
    }

    public List<BookingDto> listOfBookingToDto(List<Booking> bookings) {
        return bookings.stream().map(BookingMapper::modelToDto).toList();
    }

    public static Booking dtoToModel(BookingDto bookingDto) {
        // start=2025-06-01T16:12:26, end=2025-06-02T16:12:26,
        // bookingStatus=null, itemId=1, bookerId=2, booker=null, item=null
        log.info("маппинг MODEL -> Booking{}", bookingDto);
        Booking booking = new Booking();
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setBookerId(bookingDto.getBookerId());
        booking.setItemId(bookingDto.getItemId());
        log.info("маппинг результат -> Booking{}", booking);
        //start=2025-06-01T16:12:26, end=2025-06-02T16:12:26,
        // itemId=1, bookerId=2, bookingStatus=WAITING, item=null, booker=null
        return booking;
    }

}
