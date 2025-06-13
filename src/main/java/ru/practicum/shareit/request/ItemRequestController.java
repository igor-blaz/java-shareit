package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;


    @PostMapping
    public ItemRequestDto addItemRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @Valid @RequestBody ItemRequestDto itemRequestDto
    ) {
        log.info("-Запрос на создание {}", itemRequestDto);
        ItemRequest itemRequest = RequestMapper.createItemRequest(itemRequestDto);
        return itemRequestService.saveRequest(itemRequest, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
        //Зпросы, которые сделал этот пользователь+ ответы на них
        log.info("Запрос на получение");
        List<ItemRequest> itemRequests = itemRequestService.getRequestsByUserId(userId);
        return RequestMapper.createListOfDto(itemRequests);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestByRequestId(
            @PathVariable Long requestId) {
        log.info("Поиск RequestId {}", requestId);
        return itemRequestService.getByRequestId(requestId);
    }

}
