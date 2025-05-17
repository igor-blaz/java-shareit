package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.util.*;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemStorage {

    Map<Long, Item> items = new HashMap<>();

    public Item addItem(Item item) {
        log.info("!!!Добавили вещь {} {}", item.getId(), item);
        item.setId(items.size());
        items.put(item.getId(), item);
        return item;
    }

    public List<Item> getAll() {
        return items.values().stream().toList();
    }

    public Item getItem(long id) {
        return items.get(id);
    }

    public List<Item> getItemsFromUser(Long userId) {
        log.info("все ВЕЩИ {}", items.values());
        return items.values().stream().filter(item ->
                Optional.ofNullable(item.getOwner())
                        .map(User::getId)
                        .map(id -> id.equals(userId))
                        .orElse(false)
        ).toList();
    }

    public List<Item> getBySearch(String text) {

        List<Item> items = new ArrayList<>();
        items.addAll(checkName(text));
        items.addAll(checkDescription(text));
        log.info("Все, что добавилось {}", items);
        return items;
    }

    public List<Item> checkName(String text) {
        log.info("VALUES!!!  {}", items.values());
        return items.values().stream()
                .filter(item -> item.getName() != null &&
                        item.getName().toLowerCase().contains(text) &&
                        item.getAvailable())
                .toList();
    }

    public List<Item> checkDescription(String text) {
        return items.values().stream()
                .filter(item -> item.getDescription() != null &&
                        item.getDescription().toLowerCase().contains(text) &&
                        item.getAvailable())
                .toList();
    }


}
