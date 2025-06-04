package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exceptions.ItemUnavailableException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.TimeValidationException;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingStorage bookingStorage;
    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public BookingDto addBooking(Booking booking, Long userId) {
        bookingTimeValidation(booking);
        xSharerValidation(userId, booking);
        booking.setItem(itemStorage.getItem(booking.getItemId()));
        availableValidation(booking);
        booking.setBooker(userService.getUser(userId));
        log.info("Booking готов {}", booking);
        return BookingMapper.modelToDto(bookingStorage.addBooking(booking));
    }

    @Override
    public BookingDto getBookingDto(Long bookingId, Long userId) {
        Booking booking = bookingStorage.findBookingById(bookingId);
        xSharerValidation(userId, booking);

        log.info("&&&  {}  ", booking);
        return BookingMapper.modelToDto(booking);
    }

    public BookingDto getLastBooking(Long bookingId) {
        Booking booking = bookingStorage.findLastBooking(bookingId);
        return booking == null ? null : BookingMapper.modelToDto(booking);
    }

    public BookingDto getNextBooking(Long bookingId) {
        Booking booking = bookingStorage.findNextBooking(bookingId);
        return booking == null ? null : BookingMapper.modelToDto(booking);
    }

    public List<BookingDto> getBookingDtoByUserId(Long userId) {
        List<Booking> bookings = bookingStorage.findAllBookingsByUserId(userId);
        return BookingMapper.listOfBookingToDto(bookings);
    }
    public List<Booking>getAll(){
        return bookingStorage.findAll();
    }

    public List<Booking> getBookingByBookerIdAndItemId(Long bookerId, Long itemId) {
        return bookingStorage.findByBookerIdAndItemId(bookerId, itemId);
    }
    public List<BookingDto> getBookingByItemId( Long itemId) {
        List<Booking>bookings = bookingStorage.findBookingByItemId(itemId);
        return BookingMapper.listOfBookingToDto(bookings);
    }


    @Transactional
    public BookingDto updateBookingStatus(Long bookingId, boolean isApproved, Long userId) {

        Booking booking = bookingStorage.findBookingById(bookingId);

        log.info("updateBookingStatus  {}  ", booking);
        xSharerValidation(userId, booking);
        return BookingMapper.modelToDto(
                bookingStorage.updateBookingStatus(booking, isApproved));
    }

    private void xSharerValidation(Long userId, Booking booking) {
        if (userId == null) {
            throw new NotFoundException("Не задан заголовок xSharer");
        } else if (booking.getBookerId() == null) {
            throw new NotFoundException("Пользователь не существует ");
        } else if (!userId.equals(booking.getBookerId()) && !userId.equals(booking.getItem().getOwnerId())) {
            throw new ItemUnavailableException("ItemUnavailableException 400-xSharerValidation");
        }
    }

    private void availableValidation(Booking booking) {
        if (!booking.getItem().getAvailable()) {
            throw new ItemUnavailableException("ItemUnavailableException 400-availableValidation");
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
        } else if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new TimeValidationException("Начало бронирования не может быть в прошлом");
        }
    }


}
