package ru.practicum.shareit.user.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

@Component
@Slf4j
public class UserDtoMapper {
    public User userDtoConverter(UserDto userDto) {
        User user = new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail());
        log.info("Mapper {}", user);
        return user;
    }
}
