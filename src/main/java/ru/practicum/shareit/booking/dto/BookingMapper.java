package ru.practicum.shareit.booking.dto;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.List;


@Slf4j
@UtilityClass
public class BookingMapper {
    public static BookingDto modelToDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                new BookingDto.Booker(booking.getBooker().getId(), booking.getBooker().getName()),
                new BookingDto.Item(booking.getItem().getId(), booking.getItem().getName()),
                booking.getBookingStatus()
        );
    }


    public List<BookingDto> listOfBookingToDto(List<Booking> bookings) {
        return bookings.stream().map(BookingMapper::modelToDto).toList();
    }

    //    public static Booking dtoToModel(BookingDto bookingDto) {
//        log.info("маппинг MODEL -> Booking{}", bookingDto);
//        Booking booking = new Booking();
//        booking.setStart(bookingDto.getStart());
//        booking.setEnd(bookingDto.getEnd());
//        booking.setBookerId(bookingDto.getBookerId());
//        booking.setItemId(bookingDto.getItemId());
//        log.info("маппинг результат -> Booking{}", booking);
//        return booking;
//    }
    public static Booking requestDtoToModel(BookingItemRequestDto dto, Long userId) {
        Booking booking = new Booking();
        booking.setStart(dto.getStart());
        booking.setEnd(dto.getEnd());
        booking.setBookingStatus(BookingStatus.WAITING);

        Item item = new Item();
        item.setId(dto.getItemId());
        booking.setItem(item);

        User booker = new User();
        booker.setId(userId);
        booking.setBooker(booker);

        return booking;
    }

}
