package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * TODO Sprint add-controllers.
 */
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
}
