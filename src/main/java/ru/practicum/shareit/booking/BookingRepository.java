package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Booking b SET b.bookingStatus = :status WHERE b.id = :id")
    int updateStatus(@Param("id") Long id, @Param("status") BookingStatus status);

    List<Booking>findAllByBooker(User booker);
    List<Booking>findAllByBookerId(Long id);
}
