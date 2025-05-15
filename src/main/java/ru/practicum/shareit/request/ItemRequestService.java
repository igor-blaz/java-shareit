package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemStorage;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.user.User;

@Service
@Slf4j
@AllArgsConstructor
public class ItemRequestService {

    private final ItemRequestStorage itemRequestStorage;
    private final ItemMapper mapper;


    public ItemRequest createRequest(long id){
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(id);
        return itemRequest;
    }

}
