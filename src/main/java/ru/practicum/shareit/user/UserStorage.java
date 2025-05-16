package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.DuplicateEmailException;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserStorage {

    private final Set<User> users = new HashSet<>();


    public User addUser(User user) {
        isUniqueEmail(user.getEmail());
        long id = users.size();
        user.setId(id);
        users.add(user);
        return user;
    }


    public void isUserOwnerOfThisEmail(String email, long id) {
        User user = getUser(id);
        if (!user.getEmail().equals(email)) {
            throw new NotFoundException("Email " + email + "уже занят");
        }
    }

    public void deleteUser(long id) {
        users.removeIf(user -> user.getId() == id);
    }

    public User getUserById(long id) {
        return getUser(id);

    }

    public void isUniqueEmail(String newEmail) {
        boolean notUnique = users.stream()
                .map(User::getEmail)
                .anyMatch(email -> email.equals(newEmail));
        if (notUnique) {
            log.info("Исключение! Email не уникален!");
            throw new DuplicateEmailException("Ошибка. Такой Email уже зарегистрирован");
        }
    }

    public User getUser(long id) {
        Optional<User> userOptional = users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new NotFoundException("Пользователя с id " + id + "не существует");
        }
    }

    public void isRealId(long id) {
        boolean isReal = users.stream().map(User::getId).anyMatch(anyId -> anyId == id);
        if (!isReal) {
            throw new NotFoundException("Пользователя с id" + id + "не существует");
        }
    }


}
