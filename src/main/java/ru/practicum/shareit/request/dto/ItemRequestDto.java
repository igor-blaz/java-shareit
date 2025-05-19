package ru.practicum.shareit.request.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@RequiredArgsConstructor
public class ItemRequestDto {
    long id = 0;
    private String description;
    private User requestor;
    private LocalDateTime created;
}
