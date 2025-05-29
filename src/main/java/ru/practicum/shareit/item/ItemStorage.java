package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.repository.ItemRepository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ItemStorage {
    private final ItemRepository itemRepository;

    public Item addItem(Item item) {
        log.info("!!!Добавили вещь {} {}", item.getId(), item);
        itemRepository.save(item);
        return item;
    }


    public Item getItem(long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id: " + id + "не найден"));
    }

    public List<Item> getItemsFromUser(Long userId) {
        return itemRepository.findAllByOwnerId(userId);
    }

    public List<Item> searchByText(String text) {

        return itemRepository.searchByNameAndDescription(text);
    }


}
