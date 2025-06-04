package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentNewDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.Collections;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemServiceImpl itemService;

    @PostMapping
    public ItemDto createItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody ItemDto itemDto
    ) {
        log.info("Запрос на создание Item {}", itemDto);
        Item item = ItemMapper.dtoToModel(itemDto);
        return itemService.addItem(item, userId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsFromUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Зпрос на получение вещей у юзера {}", userId);
        return itemService.getItemsFromUser(userId);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(@PathVariable Long itemId,
                                  @RequestBody CommentNewDto newComment,
                                  @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("!!!!!!!!!!!!!!!!!!!COMMENT!!!!!");
        Comment comment = new Comment();
        comment.setText(newComment.getText());
        log.info("Комментарий {} для Item {}", comment, itemId);
        Comment commentModel = itemService.saveComment(comment, userId, itemId);
        return CommentMapper.createCommentDto(commentModel);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable long itemId,
                              @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("ПАТЧ {}", itemDto);
        Item item = ItemMapper.dtoToModel(itemDto);
        return itemService.updateItem(itemId, item, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemByUserId(@PathVariable long itemId) {
        log.info("Зпрос на получение вещей");
        return itemService.getItem(itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchByText(@RequestParam String text) {
        log.info("поиск по тексту {}", text);
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return itemService.searchByText(text);
    }


}
