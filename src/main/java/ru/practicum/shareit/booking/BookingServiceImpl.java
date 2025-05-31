package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exceptions.AccessDeniedException;
import ru.practicum.shareit.exceptions.ItemUnavailableException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.TimeValidationException;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingStorage bookingStorage;
    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public BookingDto addBooking(Booking booking, Long userId) {
        log.info("Добавление в Service {}  ", booking);
        xSharerValidation(userId, booking);
        bookingTimeValidation(booking);
        booking.setItem(itemStorage.getItem(booking.getItemId()));
        availableValidation(booking);
        booking.setBooker(userService.getUser(userId));
        log.info("Получившийся booking {}", booking);
        return BookingMapper.modelToDto(booking);
    }

    @Override
    public BookingDto getBookingDto(Long id) {
        Booking booking = bookingStorage.findBookingById(id);
        return BookingMapper.modelToDto(booking);
    }

    public BookingDto updateBookingStatus(Long bookingId, boolean isApproved, Long userId) {
        xSharerValidation(userId, bookingStorage.findBookingById(bookingId));
        Booking booking = bookingStorage.updateBookingStatus(bookingId, isApproved);
        return BookingMapper.modelToDto(booking);
    }

    private void xSharerValidation(Long userId, Booking booking) {
        if (userId == null) {
            throw new NotFoundException("Не задан заголовок xSharer");
        } else if (booking.getBookerId() == null) {
            throw new NotFoundException("Пользователь не существует ");
        } else if (!userId.equals(booking.getBookerId())) {
            throw new AccessDeniedException("Этот Booking не принадлежит данному пользователю");
        }
    }

    private void availableValidation(Booking booking) {
        if (!booking.getItem().getAvailable()) {
            throw new ItemUnavailableException("Вещь недоступна");
        }
    }

    private void bookingTimeValidation(Booking booking) {
        if (booking.getStart().isAfter(booking.getEnd())) {
            throw new TimeValidationException("Начало бронирования позже окончания");
        } else if (booking.getEnd().isBefore(booking.getStart())) {
            throw new TimeValidationException("Окончание бронирования раньше начала");
        } else if (booking.getStart().isEqual(booking.getEnd())) {
            throw new TimeValidationException("Окончание и начало не " +
                    "могут быть в одно и то же время ");
        } else if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new TimeValidationException("Окончание бронирования не может быть в прошлом");
        }
    }


}
