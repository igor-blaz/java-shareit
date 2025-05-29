package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.DuplicateEmailException;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.repository.UserRepository;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
@EnableJpaRepositories(basePackages = "ru.practicum.shareit.repository")

public class UserStorage {
    private final UserRepository userRepository;


    public User addUser(User user) {
        isUniqueEmail(user.getEmail());
        userRepository.save(user);
        return user;
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("id" + id + "не найден"));

    }

    public void isUniqueEmail(String newEmail) {
        if (userRepository.existsByEmail(newEmail)) {
            log.info("Исключение! Email не уникален!");
            throw new DuplicateEmailException("Ошибка. Такой Email уже зарегистрирован");
        }
    }


}
