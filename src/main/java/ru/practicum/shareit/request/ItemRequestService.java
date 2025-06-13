package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ItemRequestService {

    private final ItemRequestStorage itemRequestStorage;
    private final UserService userService;
    private final ItemServiceImpl itemService;


    public ItemRequest createRequest(long id) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(id);
        return itemRequest;
    }

    public ItemRequestDto saveRequest(ItemRequest itemRequest, Long userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
        itemRequest.setRequestor(user);
        itemRequest.setId(userId);
        itemRequestStorage.saveItemRequest(itemRequest);
        return RequestMapper.createDto(itemRequest);
    }

    public List<ItemRequest> getRequestsByUserId(Long userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
        List<ItemRequest> itemRequestList = itemRequestStorage.getItemRequestByUserId(userId);
        List<ItemRequestDto> DtoList = RequestMapper.createListOfDto(itemRequestList);
        setItemDtoOnRequest(DtoList);
        return itemRequestList;

    }

    public ItemRequestDto getByRequestId(Long requestId) {
        ItemRequest itemRequest = itemRequestStorage.getByRequestId(requestId);
        ItemRequestDto requestDto = RequestMapper.createDto(itemRequest);
        requestDto.setItems(itemService.findItemsByRequestId(requestId));
        return requestDto;
    }

    private void setItemDtoOnRequest(List<ItemRequestDto> itemRequests) {
        for (ItemRequestDto itemRequestDto : itemRequests) {
            itemRequestDto.setItems(itemService.findItemsByRequestId(itemRequestDto.getId()));
        }


    }

}
