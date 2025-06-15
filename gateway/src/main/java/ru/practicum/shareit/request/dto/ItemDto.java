package ru.practicum.shareit.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.BookingDto;

import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    private String description;
    @NotNull
    private Boolean available;
    private Long requestId;
    private Long ownerId;

    BookingDto lastBooking;
    BookingDto nextBooking;
    private List<CommentDto> comments;


}
