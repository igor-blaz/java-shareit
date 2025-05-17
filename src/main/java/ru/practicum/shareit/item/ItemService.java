package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

public interface ItemService {
    ItemDto addItem(Item item, Long userId);

    ItemDto getItem(Long id);

    ItemDto patchItem(Long oldItemId, Item enhansedItem, Long userId);
}
