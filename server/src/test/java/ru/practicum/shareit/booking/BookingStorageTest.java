package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingStorageTest {

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingStorage bookingStorage;

    private Booking booking;

    @BeforeEach
    void setUp() {
        booking = new Booking();
        booking.setId(1L);
    }

    @Test
    void findBookingById_shouldReturnBooking() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        Booking result = bookingStorage.findBookingById(1L);

        assertThat(result).isEqualTo(booking);
        verify(bookingRepository).findById(1L);
    }

    @Test
    void findBookingById_shouldThrowIfNotFound() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookingStorage.findBookingById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("не найден");
    }

    @Test
    void addBooking_shouldSaveAndReturn() {
        when(bookingRepository.save(booking)).thenReturn(booking);

        Booking result = bookingStorage.addBooking(booking);

        assertThat(result).isEqualTo(booking);
        verify(bookingRepository).save(booking);
    }

    @Test
    void findAllBookingsByUserId_shouldReturnList() {
        when(bookingRepository.findAllByBookerId(1L)).thenReturn(List.of(booking));

        List<Booking> result = bookingStorage.findAllBookingsByUserId(1L);

        assertThat(result).containsExactly(booking);
    }

    @Test
    void findByBookerIdAndItemId_shouldReturnList() {
        when(bookingRepository.findAllByBookerIdAndItemId(1L, 2L)).thenReturn(List.of(booking));

        List<Booking> result = bookingStorage.findByBookerIdAndItemId(1L, 2L);

        assertThat(result).containsExactly(booking);
    }
}