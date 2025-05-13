package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.DuplicateEmailException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.dto.MapperDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserStorage {

    private final MapperDto mapperDto;
    private final Set<User> users = new HashSet<>();


    public UserDto addUser(User user) {
        isUniqueEmail(user.getEmail());
        long id = users.size();
        user.setId(id);
        users.add(user);
        return mapperDto.userDtoConverter(user);
    }

    public UserDto getUserById(long id) {
        Optional<User> userOptional = users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return mapperDto.userDtoConverter(user);
        } else {
            throw new NotFoundException("Пользователя с id " + id + "не существует");
        }
    }

    private void isUniqueEmail(String newEmail) {
        boolean notUnique = users.stream()
                .map(User::getEmail)
                .anyMatch(email -> email.equals(newEmail));
        if (notUnique) {
            log.info("Исключение! Email не уникален!");
            throw new DuplicateEmailException("Ошибка. Такой Email уже зарегистрирован");
        }
    }


}
