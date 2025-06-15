package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentNewDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collections;

@Slf4j
@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @Valid @RequestBody ItemDto itemDto) {
        log.info("Создание предмета: {}", itemDto);
        return itemClient.createItem(userId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsFromUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получение всех предметов пользователя: {}", userId);
        return itemClient.getAllItemsFromUser(userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(@PathVariable Long itemId,
                                              @RequestBody CommentNewDto newComment,
                                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Добавление комментария к item {}: {}", itemId, newComment);
        return itemClient.postComment(userId, itemId, newComment);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable Long itemId,
                                             @RequestBody ItemDto itemDto,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Обновление item {}: {}", itemId, itemDto);
        return itemClient.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@PathVariable Long itemId,
                                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получение item {} пользователем {}", itemId, userId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchByText(@RequestParam String text) {
        log.info("Поиск по тексту: {}", text);
        return text.isBlank()
                ? ResponseEntity.ok(Collections.emptyList())
                : itemClient.searchByText(text);
    }
}
