package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exceptions.AccessDeniedException;
import ru.practicum.shareit.exceptions.ItemUnavailableException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserService userService;
    private final BookingServiceImpl bookingService;

    @Override
    public ItemDto addItem(Item item, Long userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
        item.setOwner(user);
        itemStorage.addItem(item);
        log.info("создана вещь {}", item);
        return ItemMapper.modelToDto(item);
    }

    public List<ItemDto> findItemsByRequestId(Long id) {
        return ItemMapper.modelArrayToDto(itemStorage.getItemByRequestId(id));
    }

    public List<ItemDto> searchByText(String text) {
        List<Item> items = itemStorage.searchByText(text);
        return ItemMapper.modelArrayToDto(items);
    }

    @Override
    public ItemDto getItem(Long id, Long userId) {

        ItemDto itemDto = ItemMapper.modelToDto(itemStorage.getItem(id));
        itemDto.setComments(findComments(id));
        if (itemDto.getOwnerId().equals(userId)) {
            setLastAndNextBookingTime(id, itemDto);
        }
        return itemDto;
    }

    private void setLastAndNextBookingTime(Long id, ItemDto itemDto) {
        List<BookingDto> bookings = bookingService.getBookingByItemId(id);
        LocalDateTime now = LocalDateTime.now();
        log.info("СЕЙЧАС {},  ", now);
        Optional<BookingDto> lastBooking = bookings.stream()
                .filter(b -> b.getEnd().isBefore(now) && b.getStatus() == BookingStatus.APPROVED) // только завершённые
                .max(Comparator.comparing(BookingDto::getEnd));

        Optional<BookingDto> nextBooking = bookings.stream()
                .filter(b -> b.getStart().isAfter(now) && b.getStatus() == BookingStatus.APPROVED) // только будущие
                .min(Comparator.comparing(BookingDto::getStart));

        itemDto.setLastBooking(lastBooking.orElse(null));
        itemDto.setNextBooking(nextBooking.orElse(null));
    }

    public List<ItemDto> getItemsFromUser(Long userId) {
        List<Item> items = itemStorage.getItemsFromUser(userId);
        return ItemMapper.modelArrayToDto(items);
    }

    public List<CommentDto> findComments(Long id) {
        List<Comment> comments = itemStorage.getComments(id);
        return CommentMapper.createListOfCommentDto(comments);
    }

    @Transactional
    public Comment saveComment(Comment comment, Long userId, Long itemId) {

        comment.setItemId(itemId);
        comment.setAuthorId(userId);
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(userService.getUser(userId));
        comment.setItem(itemStorage.getItem(itemId));
        log.info("Сoхраняем коммент {}", comment);
        commentValidation(comment, userId, itemId);

        return itemStorage.addComment(comment);
    }

    public void commentValidation(Comment comment, Long userId, Long itemId) {
        List<Booking> bookings = bookingService.getBookingByBookerIdAndItemId(userId, itemId);
        log.info("BOOKINGS  Размер   {}", bookings.size());
        for (Booking booking : bookings) {
            if (booking.getEnd().isAfter(LocalDateTime.now())) {
                log.info("BAD");
                throw new ItemUnavailableException("Комментировать можно только после окончания аренды");
            }
            if (!booking.getItem().getId().equals(comment.getItemId())) {
                log.info("BAD");
                throw new ItemUnavailableException("Пользователь не бронировал эту вещь");
            }
            if (!comment.getItem().getAvailable()) {
                log.info("BAD");
                throw new ItemUnavailableException("Вещь недоступна");
            }


        }
        log.info("Валидация {} прошла успешно", comment.getId());
    }

    @Override
    public ItemDto updateItem(Long oldItemId, Item enhansedItem, Long userId) {

        Item item = itemStorage.getItem(oldItemId);
        if (item == null) {
            throw new NotFoundException("Item with id=" + oldItemId + " not found");
        }
        updateName(item, enhansedItem);
        updateDescription(item, enhansedItem);
        updateAvailable(item, enhansedItem);
        xSharerValidation(userId, item);
        return ItemMapper.modelToDto(item);
    }

    private void updateName(Item item, Item enhansedItem) {

        if (!item.getName().equals(enhansedItem.getName())) {
            item.setName(enhansedItem.getName());
        }
    }

    private void updateDescription(Item item, Item enhansedItem) {
        if (!item.getDescription().equals(enhansedItem.getDescription())) {
            item.setDescription(enhansedItem.getDescription());
        }
    }

    private void updateAvailable(Item item, Item enhansedItem) {
        if (!item.getAvailable().equals(enhansedItem.getAvailable())) {
            item.setAvailable(enhansedItem.getAvailable());
        }
    }

    private void xSharerValidation(Long userId, Item item) {
        if (userId == null) {
            throw new NotFoundException("Не задан заголовок sSharer");
        } else if (item.getOwner().getId() == null) {
            throw new NotFoundException("Пользователь не существует ");
        } else if (!userId.equals(item.getOwner().getId())) {
            throw new AccessDeniedException("Эта Item не принадлежит данному пользователю");
        }
    }

}
