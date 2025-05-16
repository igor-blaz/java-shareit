package ru.practicum.shareit.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;

    public UserDto addUser(User user) {
        userStorage.addUser(user);
        return userMapper.userDtoConverter(user);
    }
    public void isRealUserId(long id){
        userStorage.isRealId(id);
    }

    public UserDto getUserDto(long id) {
        User user = userStorage.getUserById(id);
        return userMapper.userDtoConverter(user);
    }

    public void deleteUser(long id) {
        userStorage.deleteUser(id);
    }
    public User getUser(Long id){
        return userStorage.getUserById(id);
    }



    public UserDto patchUser(long id, User enhansedUser) {

        User user = userStorage.getUser(id);
        log.info("enhansedUser - {}", enhansedUser);
        // userStorage.isUserOwnerOfThisEmail(enhansedUser.getEmail(), id);
        patchEmail(user, enhansedUser);
        patchName(user, enhansedUser);
        return userMapper.userDtoConverter(user);
    }

    private void patchName(User user, User enhansedUser) {
        if (enhansedUser.getName() == null) {
            return;
        }
        if (!user.getName().equals(enhansedUser.getName())) {
            user.setName(enhansedUser.getName());
        }
    }

    private void patchEmail(User user, User enhansedUser) {
        if (enhansedUser.getEmail() == null) {
            return;
        }
        if (!user.getEmail().equals(enhansedUser.getEmail())) {
            userStorage.isUniqueEmail(enhansedUser.getEmail());
            user.setEmail(enhansedUser.getEmail());
        }
    }

}
