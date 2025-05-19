package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ItemRequestService {

    private final ItemRequestStorage itemRequestStorage;


    public ItemRequest createRequest(long id) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(id);
        return itemRequest;
    }

}
