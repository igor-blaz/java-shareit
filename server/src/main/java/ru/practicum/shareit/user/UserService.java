package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserRequestDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public UserDto addUser(UserRequestDto userRequestDto) {
        User user = UserMapper.fromRequestDto(userRequestDto);
        userStorage.addUser(user);
        log.info("Добавлен пользователь {}", user);
        return UserMapper.userDtoConverter(user);
    }

    public UserDto getUserDto(long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new NotFoundException("User with id=" + id + " not found");
        }
        return UserMapper.userDtoConverter(user);
    }

    public void deleteUser(long id) {
        userStorage.deleteUser(id);
    }

    public User getUser(Long id) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new NotFoundException("User with id=" + id + " not found");
        }
        return user;
    }


    public UserDto updateUser(long id, User enhansedUser) {
        User user = userStorage.getUserById(id);
        if (user == null) {
            throw new NotFoundException("User with id=" + id + " not found");
        }
        log.info("enhansedUser - {}", enhansedUser);
        updateEmail(user, enhansedUser);
        updateName(user, enhansedUser);
        return UserMapper.userDtoConverter(user);
    }

    private void updateName(User user, User enhansedUser) {
        if (enhansedUser.getName() == null) {
            return;
        }
        if (!user.getName().equals(enhansedUser.getName())) {
            user.setName(enhansedUser.getName());
        }
    }

    private void updateEmail(User user, User enhansedUser) {
        if (enhansedUser.getEmail() == null) {
            return;
        }
        if (!user.getEmail().equals(enhansedUser.getEmail())) {

            userStorage.isUniqueEmail(enhansedUser.getEmail());
            user.setEmail(enhansedUser.getEmail());
        }
    }

}
