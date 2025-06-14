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
        log.info("создание запроса {}", itemRequestDto);
        ItemRequest itemRequest = RequestMapper.createItemRequest(itemRequestDto);
        log.info("Entity {}", itemRequest);
        return itemRequestService.saveRequest(itemRequest, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getItemRequest(
            @RequestHeader("X-Sharer-User-Id") Long userId
    ) {
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

    @GetMapping("/all")
    public List<ItemRequestDto> getAllNotMine(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<ItemRequest> requests = itemRequestService.getAllNotMine(userId);
        return RequestMapper.createListOfDto(requests);

    }

}
