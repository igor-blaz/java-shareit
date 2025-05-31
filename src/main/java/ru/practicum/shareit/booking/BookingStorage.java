package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.User;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
@EnableJpaRepositories(basePackages = "ru/practicum/shareit/booking")
public class BookingStorage {
    private final BookingRepository bookingRepository;

    public Booking findBookingById(long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Bookind id: " + id + "не найден"));
    }
    public List<Booking> findAllBookingsByUserId(User user){
        return bookingRepository.findAllByBooker(user);
    }

    public Booking addBooking(Booking booking) {
        log.info("Сервис. Добавление Booking-----{}", booking);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking updateBookingStatus(Long bookingId, boolean isApproved) {
        BookingStatus bookingStatus;
        if (isApproved) {
            bookingStatus = BookingStatus.APPROVED;
        } else {
            bookingStatus = BookingStatus.REJECTED;
        }
        int rowsChanged = bookingRepository.updateStatus(bookingId, bookingStatus);
        if (rowsChanged < 0) {
            throw new NotFoundException("Статус не обновлен");
        }
        return findBookingById(bookingId);
    }


}
