package ru.practicum.shareit.user.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

@Slf4j
@Component
public class MapperDto {

    public UserDto userDtoConverter(User user) {
        UserDto userDto = new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
        log.info("Mapper {}", userDto);
        return userDto;
    }
}
