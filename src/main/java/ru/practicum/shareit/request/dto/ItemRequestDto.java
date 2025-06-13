package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {
        Long id;
        @NotNull
        private String description;
        private Long requestorId;
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        private LocalDateTime created = LocalDateTime.now();
        List<ItemDto> items = new ArrayList<>();

        public void addItems(List<ItemDto> items) {
                if (items != null) {
                        this.items.addAll(items);
                }
        }

}
