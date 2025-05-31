package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Booking b SET b.bookingStatus = :status WHERE b.id = :id")
    int updateStatus(@Param("id") Long id, @Param("status") BookingStatus status);
}
