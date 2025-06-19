package ru.practicum.shareit.booking;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    private List<Booking> bookingList;
    private BookingState bookingState;
    private Booking booking;
    private LocalDateTime now;
    private User defaultUser;
    private Booking waitingBooking;
    private Booking rejectedBooking;


    @BeforeEach
    void setUp() {
        bookingList = Instancio.ofList(Booking.class)
                .size(10).create();
        booking = Instancio.create(Booking.class);
        now = LocalDateTime.now();
        defaultUser = Instancio.create(User.class);
        waitingBooking = Instancio.create(Booking.class);
        waitingBooking.setBookingStatus(BookingStatus.WAITING);
        rejectedBooking = Instancio.create(Booking.class);
        rejectedBooking.setBookingStatus(BookingStatus.REJECTED);
    }

    @Test
    void updateBookingStatusTest() {
        Long userId = 1L;
        Long ownerId = 2L;
        Long bookingId = 10L;

        Item item = new Item();
        item.setId(100L);
        item.setOwner(new User(ownerId, "Owner", "owner@mail.ru"));

        User booker = new User(userId, "User", "user@mail.ru");

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setBookingStatus(BookingStatus.WAITING);

        Booking updatedBooking = new Booking();
        updatedBooking.setId(bookingId);
        updatedBooking.setBooker(booker);
        updatedBooking.setItem(item);
        updatedBooking.setBookingStatus(BookingStatus.APPROVED);

        when(bookingStorage.findBookingById(bookingId)).thenReturn(booking);
        when(bookingStorage.updateBookingStatus(booking, true)).thenReturn(updatedBooking);

        BookingDto result = bookingService.updateBookingStatus(bookingId, true, ownerId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(bookingId);
        assertThat(result.getStatus()).isEqualTo(BookingStatus.APPROVED);

        verify(bookingStorage).findBookingById(bookingId);
        verify(bookingStorage).updateBookingStatus(booking, true);
    }

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

        List<BookingDto> resultCurrent = bookingService.getBookingDtoByUserId(userId, BookingState.CURRENT);


        assertThat(resultCurrent).hasSize(1);
        assertThat(resultCurrent.get(0).getStart()).isBefore(now);
        assertThat(resultCurrent.get(0).getEnd()).isAfter(now);

        verify(bookingStorage).findAllBookingsByUserId(userId);
    }

    @Test
    void getBookingDtoByUserId_FutureBookingTest() {

        bookingState = BookingState.FUTURE;
        booking.setStart(now.plusDays(100));
        booking.setEnd(now.plusDays(200));
        booking.setBooker(defaultUser);
        when(bookingStorage.findAllBookingsByUserId(anyLong())).thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getBookingDtoByUserId(defaultUser.getId(), bookingState);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStart()).isAfter(now);
        assertThat(result.get(0).getEnd()).isAfter(now);

        verify(bookingStorage).findAllBookingsByUserId(defaultUser.getId());

    }

    @Test
    void getBookingDtoByUserId_CurrentBookingTest() {

        bookingState = BookingState.CURRENT;
        booking.setStart(now.minusDays(100));
        booking.setEnd(now.plusDays(200));
        booking.setBooker(defaultUser);
        when(bookingStorage.findAllBookingsByUserId(anyLong())).thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getBookingDtoByUserId(defaultUser.getId(), bookingState);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStart()).isBefore(now);
        assertThat(result.get(0).getEnd()).isAfter(now);

        verify(bookingStorage).findAllBookingsByUserId(defaultUser.getId());

    }

    @Test
    void getBookingDtoByUserId_PastBookingTest() {

        bookingState = BookingState.PAST;
        booking.setStart(now.minusDays(900));
        booking.setEnd(now.minusDays(200));
        booking.setBooker(defaultUser);
        when(bookingStorage.findAllBookingsByUserId(anyLong())).thenReturn(List.of(booking));
        List<BookingDto> result = bookingService.getBookingDtoByUserId(defaultUser.getId(), bookingState);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStart()).isBefore(now);
        assertThat(result.get(0).getEnd()).isBefore(now);

        verify(bookingStorage).findAllBookingsByUserId(defaultUser.getId());

    }

    @Test
    void getBookingDtoByUserId_RejectedBookingTest() {

        bookingState = BookingState.REJECTED;
        booking.setStart(now.plusDays(100));
        booking.setEnd(now.plusDays(800));
        booking.setBooker(defaultUser);
        booking.setBookingStatus(BookingStatus.REJECTED);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(waitingBooking);
        when(bookingStorage.findAllBookingsByUserId(anyLong())).thenReturn(bookings);
        List<BookingDto> result = bookingService.getBookingDtoByUserId(defaultUser.getId(), bookingState);

        assertThat(result).hasSize(1);
        verify(bookingStorage).findAllBookingsByUserId(defaultUser.getId());

    }

    @Test
    void getBookingDtoByUserId_WaitingBookingTest() {

        bookingState = BookingState.WAITING;
        booking.setStart(now.plusDays(100));
        booking.setEnd(now.plusDays(800));
        booking.setBooker(defaultUser);
        booking.setBookingStatus(BookingStatus.WAITING);
        List<Booking> bookings = new ArrayList<>();
        bookings.add(booking);
        bookings.add(rejectedBooking);
        when(bookingStorage.findAllBookingsByUserId(anyLong())).thenReturn(bookings);
        List<BookingDto> result = bookingService.getBookingDtoByUserId(defaultUser.getId(), bookingState);

        assertThat(result).hasSize(1);
        verify(bookingStorage).findAllBookingsByUserId(defaultUser.getId());

    }

    @Test
    void getBookingDtoByUserId_AllBookingTest() {
        bookingState = BookingState.ALL;
        when(bookingStorage.findAllBookingsByUserId(anyLong())).thenReturn(bookingList);
        List<BookingDto> result = bookingService.getBookingDtoByUserId(anyLong(), bookingState);
        assertThat(result).hasSize(10);
    }

}
