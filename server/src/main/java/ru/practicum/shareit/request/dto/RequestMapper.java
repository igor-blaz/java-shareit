package ru.practicum.shareit.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.request.ItemRequest;

import java.util.List;

@UtilityClass
public class RequestMapper {

    public static ItemRequestDto createDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setCreated(itemRequest.getCreated());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setRequestorId(itemRequest.getRequestor().getId());
        return itemRequestDto;

    }

    public static ItemRequest createItemRequest(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setCreated(itemRequestDto.getCreated());
        itemRequest.setDescription(itemRequestDto.getDescription());
        return itemRequest;
    }

    public static List<ItemRequestDto> createListOfDto(List<ItemRequest> itemRequestList) {
        return itemRequestList.stream().map(RequestMapper::createDto).toList();
    }

}
