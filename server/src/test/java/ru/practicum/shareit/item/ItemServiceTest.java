package ru.practicum.shareit.item;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

    @Test
    void addItem_shouldSaveAndReturnDto() {
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
    void saveComment_shouldSaveComment() {
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
    void updateItem_shouldUpdateFields() {
        Long userId = 1L;
        Long itemId = 5L;

        User user = Instancio.create(User.class);
        user.setId(userId);

        Item oldItem = Instancio.create(Item.class);
        oldItem.setId(itemId);
        oldItem.setOwner(user);
        oldItem.setName("Old Name");
        oldItem.setDescription("Old Desc");
        oldItem.setAvailable(true);

        Item newItem = new Item();
        newItem.setName("New Name");
        newItem.setDescription("New Desc");
        newItem.setAvailable(false);

        when(itemStorage.getItem(itemId)).thenReturn(oldItem);

        ItemDto result = itemService.updateItem(itemId, newItem, userId);

        assertThat(result).isNotNull();
        assertThat(oldItem.getName()).isEqualTo("New Name");
        assertThat(oldItem.getDescription()).isEqualTo("New Desc");
        assertThat(oldItem.getAvailable()).isFalse();
        verify(itemStorage).getItem(itemId);
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

