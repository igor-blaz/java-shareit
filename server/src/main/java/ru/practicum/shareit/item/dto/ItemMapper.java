package ru.practicum.shareit.item.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


@UtilityClass
public class ItemMapper {
    public static Item dtoToModel(ItemDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setRequestId(itemDto.getRequestId());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    public static List<ItemDto> modelArrayToDto(List<Item> items) {
        return items.stream().map(ItemMapper::modelToDto).toList();
    }



    public static ItemDto modelToDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setRequestId(item.getRequestId());
        itemDto.setOwnerId(item.getOwner().getId());
        return itemDto;

    }
}
