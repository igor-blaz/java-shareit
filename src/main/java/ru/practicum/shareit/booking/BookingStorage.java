package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundException;

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

    public List<Booking> findAllBookingsByUserId(Long id) {
        return bookingRepository.findAllByBookerId(id);
    }

    public Booking addBooking(Booking booking) {
        log.info("Сервис. Добавление Booking-----{}", booking);
        return bookingRepository.save(booking);
    }

    public List<Booking> findByBookerIdAndItemId(Long bookerId, Long itemId) {
        return bookingRepository.findAllByBookerIdAndItemId(bookerId, itemId);
    }

    @Transactional
    public Booking updateBookingStatus(Booking booking, boolean isApproved) {
        BookingStatus bookingStatus;
        if (isApproved) {
            bookingStatus = BookingStatus.APPROVED;
        } else {
            bookingStatus = BookingStatus.REJECTED;
        }
        int rowsChanged = bookingRepository.updateStatus(booking.getId(), bookingStatus);
        if (rowsChanged < 0) {
            throw new NotFoundException("Статус не обновлен");
        } else {
            log.info("Статус обновлен!!!!!!");
        }

        return findBookingById(booking.getId());
    }


}
