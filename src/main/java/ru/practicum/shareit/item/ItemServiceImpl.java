package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.AccessDeniedException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.user.UserService;

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
        ItemRequest request = itemRequestService.createRequest(userId);
        item.setRequest(request);
        itemStorage.addItem(item);
        return mapper.modelToDto(item);
    }

    public ItemDto patchItem(Long oldItemId, Item enhansedItem, Long xSharer){

        Item item = itemStorage.getItem(oldItemId);
        patchName(item, enhansedItem);
        patchDescription(item, enhansedItem);
        patchAvailable(item, enhansedItem);
        xSharerValidation(xSharer, enhansedItem.getOwner().getId());
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

    private void xSharerValidation(Long xSharer, Long userId){
        if (xSharer == null) {
            throw new NotFoundException("Не задан заголовок sSharer");
        } else if (!userId.equals(xSharer)) {
            log.info("XSHARER{}",xSharer);
            log.info("old{}",userId);

            throw new AccessDeniedException("Эта Item не принадлежит данному пользователю");
        }
    }

}
