package ru.practicum.shareit.booking;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingItemRequestDto;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookingServiceTest {

    @Mock
    private BookingStorage bookingStorage;

    @Mock
    private ItemStorage itemStorage;

    @Mock
    private UserService userService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void addBookingTest() {
        Long userId = 1L;
        Long itemId = 10L;

        BookingItemRequestDto requestDto = new BookingItemRequestDto();
        requestDto.setItemId(itemId);
        requestDto.setStart(LocalDateTime.now().plusDays(1));
        requestDto.setEnd(LocalDateTime.now().plusDays(2));

        User user = Instancio.create(User.class);
        user.setId(userId);

        Item item = Instancio.create(Item.class);
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(Instancio.create(User.class));

        Booking booking = new Booking();
        booking.setId(100L);
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(requestDto.getStart());
        booking.setEnd(requestDto.getEnd());
        booking.setBookingStatus(BookingStatus.WAITING);

        when(itemStorage.getItem(itemId)).thenReturn(item);
        when(userService.getUser(userId)).thenReturn(user);
        when(bookingStorage.addBooking(any())).thenReturn(booking);

        BookingDto result = bookingService.addBooking(requestDto, userId);

        assertThat(result).isNotNull();
        assertThat(result.getItem().getId()).isEqualTo(itemId);
        assertThat(result.getBooker().getId()).isEqualTo(userId);

        verify(itemStorage).getItem(itemId);
        verify(userService).getUser(userId);
        verify(bookingStorage).addBooking(any());
    }

    @Test
    void getBookingDtoByUserIdTest() {
        Long userId = 1L;
        LocalDateTime now = LocalDateTime.now();

        Booking currentBooking = new Booking();
        currentBooking.setStart(now.minusHours(1));
        currentBooking.setEnd(now.plusHours(1));
        currentBooking.setBookingStatus(BookingStatus.APPROVED);
        currentBooking.setBooker(new User(userId, "Name", "email@email.com"));
        currentBooking.setItem(Instancio.create(Item.class));

        when(bookingStorage.findAllBookingsByUserId(userId))
                .thenReturn(List.of(currentBooking));

        List<BookingDto> result = bookingService.getBookingDtoByUserId(userId, BookingState.CURRENT);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStart()).isBefore(now);
        assertThat(result.get(0).getEnd()).isAfter(now);

        verify(bookingStorage).findAllBookingsByUserId(userId);
    }
}
