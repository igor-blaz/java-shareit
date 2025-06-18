package ru.practicum.shareit.item;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.AccessDeniedException;
import ru.practicum.shareit.exceptions.ItemUnavailableException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class ItemServiceTest {

    @Mock
    private ItemStorage itemStorage;

    @Mock
    private UserService userService;

    @Mock
    private BookingServiceImpl bookingService;

    @InjectMocks
    private ItemServiceImpl itemService;

    List<Booking> bookings;
    Item item;
    User user;
    Comment comment;
    LocalDateTime now;

    @BeforeEach
    void setUp() {
        bookings = Instancio.ofList(Booking.class).size(1).create();
        user = Instancio.create(User.class);
        item = Instancio.create(Item.class);
        comment = Instancio.create(Comment.class);
        now = LocalDateTime.now();
    }

    @Test
    void saveBadCommentException_unavailable() {

        item.setAvailable(false);
        comment.setAuthor(user);
        comment.setItem(item);
        itemService.commentValidation(comment, comment.getAuthor().getId(), comment.getItem().getId());

        when(bookingService.getBookingByBookerIdAndItemId(anyLong(), anyLong()))
                .thenReturn(bookings);

        assertThatThrownBy(() -> itemService.commentValidation(comment,
                comment.getAuthor().getId(), comment.getItem().getId()))
                .isInstanceOf(ItemUnavailableException.class);
    }

    @Test
    void saveBadCommentException_badTime() {

        comment.setAuthor(user);
        comment.setItem(item);
        bookings.getFirst().setStart(now);
        bookings.getFirst().setEnd(now.minusHours(99));
        itemService.commentValidation(comment, comment.getAuthor().getId(), comment.getItem().getId());

        when(bookingService.getBookingByBookerIdAndItemId(anyLong(), anyLong()))
                .thenReturn(bookings);

        assertThatThrownBy(() -> itemService.commentValidation(comment,
                comment.getAuthor().getId(), comment.getItem().getId()))
                .isInstanceOf(ItemUnavailableException.class);
    }

    @Test
    void saveBadCommentException_badOwnerId() {

        item.setOwner(null);
        comment.setAuthor(user);
        comment.setItem(item);
        itemService.commentValidation(comment, comment.getAuthor().getId(), comment.getItem().getId());

        when(bookingService.getBookingByBookerIdAndItemId(anyLong(), anyLong()))
                .thenReturn(bookings);

        assertThatThrownBy(() -> itemService.commentValidation(comment,
                comment.getAuthor().getId(), comment.getItem().getId()))
                .isInstanceOf(ItemUnavailableException.class);
    }


    @Test
    void addItemTest() {
        Long userId = 1L;
        User user = Instancio.create(User.class);
        user.setId(userId);

        Item item = Instancio.create(Item.class);

        when(userService.getUser(userId)).thenReturn(user);
        doAnswer(invocation -> {
            Item arg = invocation.getArgument(0);
            arg.setId(10L);
            return null;
        }).when(itemStorage).addItem(any(Item.class));

        ItemDto result = itemService.addItem(item, userId);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(10L);
        assertThat(item.getOwner()).isEqualTo(user);
        verify(itemStorage).addItem(item);
        verify(userService).getUser(userId);
    }

    @Test
    void getItemTest() {
        List<BookingDto> bookingDtos = Instancio.ofList(BookingDto.class).size(1).create();
        List<Comment> comments = Instancio.ofList(Comment.class).size(1).create();
        User user = Instancio.create(User.class);
        Item item = Instancio.create(Item.class);
        item.setOwner(user);
        when(bookingService.getBookingByItemId(anyLong())).thenReturn(bookingDtos);
        when(itemStorage.getComments(anyLong())).thenReturn(comments);
        when(itemStorage.getItem(1L)).thenReturn(item);


        ItemDto result = itemService.getItem(1L, user.getId());

        assertNotNull(result);
        assertEquals(item.getId(), result.getId());
        assertEquals(item.getName(), result.getName());
        assertEquals(item.getDescription(), result.getDescription());
        assertEquals(item.getAvailable(), result.getAvailable());
        assertEquals(CommentMapper.createListOfCommentDto(comments), result.getComments());

        verify(itemStorage).getItem(1L);
        verify(bookingService).getBookingByItemId(1L);
    }

    @Test
    void saveCommentTest() {
        Long userId = 1L;
        Long itemId = 2L;

        Comment comment = new Comment();
        comment.setText("Test comment");

        User user = Instancio.create(User.class);
        user.setId(userId);

        Item item = Instancio.create(Item.class);
        item.setId(itemId);
        item.setAvailable(true);

        when(userService.getUser(userId)).thenReturn(user);
        when(itemStorage.getItem(itemId)).thenReturn(item);
        when(bookingService.getBookingByBookerIdAndItemId(userId, itemId)).thenReturn(
                List.of(createPastApprovedBooking(item))
        );
        when(itemStorage.addComment(any(Comment.class))).thenAnswer(i -> i.getArgument(0));

        Comment saved = itemService.saveComment(comment, userId, itemId);

        assertThat(saved).isNotNull();
        assertThat(saved.getAuthorId()).isEqualTo(userId);
        assertThat(saved.getItemId()).isEqualTo(itemId);
        assertThat(saved.getAuthor()).isEqualTo(user);
        assertThat(saved.getItem()).isEqualTo(item);
        verify(itemStorage).addComment(any(Comment.class));
    }

    @Test
    void updateItem_shouldThrowNotFoundExceptionTest() {
        Long userId = 1L;
        Long itemId = 5L;

        Item newItem = new Item();
        newItem.setName("New Name");
        newItem.setDescription("New Desc");
        newItem.setAvailable(false);

        when(itemStorage.getItem(itemId)).thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            itemService.updateItem(itemId, newItem, userId);
        });

        verify(itemStorage).getItem(itemId);
    }

    @Test
    void getItemDto_shouldThrowNotFoundExceptionTest() {
        Long userId = 1L;
        Long itemId = 5L;

        Item newItem = new Item();
        newItem.setName("New Name");
        newItem.setDescription("New Desc");
        itemService.findItemsByRequestId(88888898L);
        assertThrows(NotFoundException.class, () -> {
            itemService.updateItem(itemId, newItem, userId);
        });
    }

    @Test
    void xSharer_shouldThrowAccessDeniedException() {
        Long userId = 12L;
        Long itemId = 5L;

        User owner = Instancio.create(User.class);
        owner.setId(99L);

        Item item = Instancio.create(Item.class);
        item.setId(itemId);
        item.setOwner(owner);
        item.setName("Old Name");
        item.setDescription("Old Desc");
        item.setAvailable(true);

        Item newItem = new Item();
        newItem.setName("New Name");
        newItem.setDescription("New Desc");
        newItem.setAvailable(false);

        when(itemService.getItem(itemId, anyLong()));


        assertThrows(AccessDeniedException.class, () -> {
            itemService.updateItem(itemId, newItem, userId);
        });

        verify(itemService).getItem(itemId, userId);
    }



    private Booking createPastApprovedBooking(Item item) {
        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStart(LocalDateTime.now().minusDays(10));
        booking.setEnd(LocalDateTime.now().minusDays(5));
        booking.setBookingStatus(BookingStatus.APPROVED);
        return booking;
    }
}

