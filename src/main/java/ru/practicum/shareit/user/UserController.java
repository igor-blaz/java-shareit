package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        User user = UserMapper.userDtoConverter(userDto);
        log.info("Запрос на добавление User {}", user);
        return userService.addUser(user);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable long userId) {
        log.info("Запрос на получение id {}", userId);
        return userService.getUserDto(userId);
    }


    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable long userId,
                             @RequestBody UserDto userDto) {
        log.info("ПАТЧ {}", userDto);
        User user = UserMapper.userDtoConverter(userDto);
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }
}
