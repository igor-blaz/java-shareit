package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public UserDto addUser(User user) {
        return userStorage.addUser(user);
    }
    public UserDto getUser(long id){
        return userStorage.getUserById(id);
    }

}
