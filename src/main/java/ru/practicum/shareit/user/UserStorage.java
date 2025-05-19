package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.DuplicateEmailException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserStorage {
    private final Map<Long, User> users = new HashMap<>();

    public User addUser(User user) {
        isUniqueEmail(user.getEmail());
        long id = users.size();
        user.setId(id);
        users.put(user.getId(), user);
        return user;
    }

    public void deleteUser(long id) {
        users.remove(id);
    }

    public User getUserById(long id) {
        return getUser(id);

    }

    public void isUniqueEmail(String newEmail) {
        boolean notUnique = users.values().stream()
                .map(User::getEmail)
                .anyMatch(email -> email.equals(newEmail));
        if (notUnique) {
            log.info("Исключение! Email не уникален!");
            throw new DuplicateEmailException("Ошибка. Такой Email уже зарегистрирован");
        }
    }

    public User getUser(long id) {
        return users.get(id);
    }

}
