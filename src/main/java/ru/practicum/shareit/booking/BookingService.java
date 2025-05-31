package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

public interface BookingService {
    BookingDto addBooking(Booking booking, Long userId);

    BookingDto getBookingDto(Long id);

}
