package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
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
    private final ItemMapper mapper;

    @PostMapping
    public ItemDto createItem(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody ItemDto itemDto
    ) {
        log.info("Запрос на создание Item {}", itemDto);
        Item item = mapper.dtoToModel(itemDto);
        return itemService.addItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@PathVariable long itemId,
                             @RequestBody ItemDto itemDto,
                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("ПАТЧ {}", itemDto);
        Item item = mapper.dtoToModel(itemDto);
        return itemService.patchItem(itemId, item, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemByUserId(@PathVariable long itemId) {
        log.info("Зпрос на получение вещей");
        return itemService.getItem(itemId);
    }

    @GetMapping
    public List<ItemDto> getAllItemsFromUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Зпрос на получение вещей у юзера {}", userId);
        return itemService.getItemsFromUser(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getBySearch(@RequestParam String text) {
        if (text.isBlank()) {
            log.info("IS BLAAAAAAAANK");
            return Collections.emptyList();
        }
        return itemService.getBySearch(text);
    }


}
