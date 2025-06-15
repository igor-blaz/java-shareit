package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> addItemRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody ItemRequestDto itemRequestDto
    ) {
        log.info("Gateway: создание запроса {}", itemRequestDto);
        return itemRequestClient.addItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getItemRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        log.info("Gateway: запрос на получение запросов пользователя {}", userId);
        return itemRequestClient.getItemRequest(userId);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestByRequestId(
            @PathVariable Long requestId
    ) {
        log.info("Gateway: поиск запроса по id {}", requestId);
        return itemRequestClient.getItemRequestByRequestId(requestId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllNotMine(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Gateway: запрос на получение всех запросов кроме пользователя {}", userId);
        return itemRequestClient.getAllNotMine(userId);
    }
}
