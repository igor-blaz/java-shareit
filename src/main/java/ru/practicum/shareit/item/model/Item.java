package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

/**
 * TODO Sprint add-controllers.
 */

@Data
@RequiredArgsConstructor
public class Item {

    private long id;
    private String name;
    private String description;
    private boolean available;
    private User owner;
    private ItemRequest request;

}
