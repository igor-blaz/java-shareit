package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

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
    public UserDto createUser(@Valid @RequestBody User user) {
        log.info("Запрос на добавление User {}", user);
        return userService.addUser(user);
    }

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        log.info("Запрос на получение id {}", userId);
        return userService.getUser(userId);
    }
    @PatchMapping("/{userId}")
    public UserDto patchUser(@PathVariable String id){
        log.info("ПАТЧ ");
        return new UserDto("d", "e");
    }
}
