package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemRequestDto;

import java.util.List;

public interface BookingService {
    BookingDto addBooking(BookingItemRequestDto bookingItemRequestDto, Long userId);

    BookingDto getBookingDto(Long id, Long userId);

    BookingDto updateBookingStatus(Long bookingId, boolean isApproved, Long userId);

    List<BookingDto> getBookingByItemId(Long itemId);

    List<Booking> getBookingByBookerIdAndItemId(Long bookerId, Long itemId);

    List<BookingDto> getBookingDtoByUserId(Long userId, BookingState state);
}
