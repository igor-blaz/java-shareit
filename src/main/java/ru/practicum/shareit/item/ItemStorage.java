package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemStorage {

    Map<Long, Item> items = new HashMap<>();

    public Item addItem(Item item){
        items.put(item.getId(),item);
        return item;
    }
    public Item getItem(long id){
        return items.get(id);
    }
}
