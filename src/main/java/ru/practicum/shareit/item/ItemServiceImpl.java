package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.AccessDeniedException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserService;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ItemServiceImpl {

    private final ItemStorage itemStorage;
    private final ItemMapper mapper;
    private final ItemRequestService itemRequestService;
    private final UserService userService;

    public ItemDto addItem(Item item, Long userId) {
        userService.isRealUserId(userId);
        User user = userService.getUser(userId);
        item.setOwner(user);
        itemStorage.addItem(item);
        log.info("создана вещь {}", item);
        return mapper.modelToDto(item);
    }

    public List<ItemDto> getBySearch(String text) {
        List<Item> items = itemStorage.getBySearch(text);
        return mapper.modelArrayToDto(items);
    }

    public ItemDto getItem(Long id) {
        return mapper.modelToDto(itemStorage.getItem(id));
    }

    public List<ItemDto> getItemsFromUser(Long userId) {
        List<Item> items = itemStorage.getItemsFromUser(userId);
        return mapper.modelArrayToDto(items);
    }

    public ItemDto patchItem(Long oldItemId, Item enhansedItem, Long userId) {

        Item item = itemStorage.getItem(oldItemId);
        patchName(item, enhansedItem);
        patchDescription(item, enhansedItem);
        patchAvailable(item, enhansedItem);
        xSharerValidation(userId, item);
        return mapper.modelToDto(item);
    }

    private void patchName(Item item, Item enhansedItem) {

        if (!item.getName().equals(enhansedItem.getName())) {
            item.setName(enhansedItem.getName());
        }
    }

    private void patchDescription(Item item, Item enhansedItem) {
        if (!item.getDescription().equals(enhansedItem.getDescription())) {
            item.setDescription(enhansedItem.getDescription());
        }
    }

    private void patchAvailable(Item item, Item enhansedItem) {
        if (!item.getAvailable().equals(enhansedItem.getAvailable())) {
            item.setAvailable(enhansedItem.getAvailable());
        }
    }

    private void xSharerValidation(Long userId, Item item) {
        if (userId == null) {
            throw new NotFoundException("Не задан заголовок sSharer");
        } else if (item.getOwner() == null) {
            throw new NotFoundException("Пользователь не существует ");
        } else if (!userId.equals(item.getOwner().getId())) {
            log.info("XSHARER{}", item.getOwner().getId());
            log.info("old{}", userId);

            throw new AccessDeniedException("Эта Item не принадлежит данному пользователю");
        }
    }

    public List<ItemDto> getAll() {
        List<Item>items =  itemStorage.getAll();
        return mapper.modelArrayToDto(items);
    }

}
