package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.AccessDeniedException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserService userService;

    @Override
    public ItemDto addItem(Item item, Long userId) {
        User user = userService.getUser(userId);
        if (user == null) {
            throw new NotFoundException("User with id=" + userId + " not found");
        }
        item.setOwnerId(userId);
        itemStorage.addItem(item);
        log.info("создана вещь {}", item);
        return ItemMapper.modelToDto(item);
    }

    public List<ItemDto> searchByText(String text) {
        List<Item> items = itemStorage.searchByText(text);
        return ItemMapper.modelArrayToDto(items);
    }

    @Override
    public ItemDto getItem(Long id) {
        return ItemMapper.modelToDto(itemStorage.getItem(id));
    }

    public List<ItemDto> getItemsFromUser(Long userId) {
        List<Item> items = itemStorage.getItemsFromUser(userId);
        return ItemMapper.modelArrayToDto(items);
    }

    @Override
    public ItemDto updateItem(Long oldItemId, Item enhansedItem, Long userId) {

        Item item = itemStorage.getItem(oldItemId);
        if (item == null) {
            throw new NotFoundException("Item with id=" + oldItemId + " not found");
        }
        updateName(item, enhansedItem);
        updateDescription(item, enhansedItem);
        updateAvailable(item, enhansedItem);
        xSharerValidation(userId, item);
        return ItemMapper.modelToDto(item);
    }

    private void updateName(Item item, Item enhansedItem) {

        if (!item.getName().equals(enhansedItem.getName())) {
            item.setName(enhansedItem.getName());
        }
    }

    private void updateDescription(Item item, Item enhansedItem) {
        if (!item.getDescription().equals(enhansedItem.getDescription())) {
            item.setDescription(enhansedItem.getDescription());
        }
    }

    private void updateAvailable(Item item, Item enhansedItem) {
        if (!item.getAvailable().equals(enhansedItem.getAvailable())) {
            item.setAvailable(enhansedItem.getAvailable());
        }
    }

    private void xSharerValidation(Long userId, Item item) {
        if (userId == null) {
            throw new NotFoundException("Не задан заголовок sSharer");
        } else if (item.getOwnerId() == null) {
            throw new NotFoundException("Пользователь не существует ");
        } else if (!userId.equals(item.getOwnerId())) {
            log.info("XSHARER{}", item.getOwnerId());
            log.info("old{}", userId);

            throw new AccessDeniedException("Эта Item не принадлежит данному пользователю");
        }
    }

}
