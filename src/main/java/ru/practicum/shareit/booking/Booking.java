package ru.practicum.shareit.booking;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@RequiredArgsConstructor
@Data
@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_booking")
    private LocalDateTime start;
    @Column(name = "end_booking")
    private LocalDateTime end;
    @Column(name = "item_id")
    private Long itemId;
    @Column(name = "booker_id")
    @NotNull
    private Long bookerId;
    @Column(name = "bookingStatus")
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus = BookingStatus.WAITING;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id", insertable = false, updatable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booker_id", insertable = false, updatable = false)
    private User booker;


}
