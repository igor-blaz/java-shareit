package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exceptions.DuplicateEmailException;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserStorageTest {


    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserStorage userStorage;

    @Test
    void addUserTest() {
        User user = new User();
        user.setEmail("unique@example.com");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User savedUser = userStorage.addUser(user);

        verify(userRepository).existsByEmail(user.getEmail());
        verify(userRepository).save(user);
        assertEquals(user, savedUser);
    }

    @Test
    void addUserErrorTest() {
        User user = new User();
        user.setEmail("duplicate@example.com");

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        DuplicateEmailException exception = assertThrows(DuplicateEmailException.class, () -> {
            userStorage.addUser(user);
        });

        assertEquals("Ошибка. Такой Email уже зарегистрирован", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUserTest() {
        Long id = 1L;

        doNothing().when(userRepository).deleteById(id);

        userStorage.deleteUser(id);

        verify(userRepository).deleteById(id);
    }

    @Test
    void getUserByIdTest() {
        Long id = 1L;
        User user = new User();
        user.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        User foundUser = userStorage.getUserById(id);

        assertEquals(user, foundUser);
    }

    @Test
    void getUserByIdErrorTest() {
        Long id = 2L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            userStorage.getUserById(id);
        });

        assertTrue(exception.getMessage().contains("UserStorage. id " + id + " не найден"));
    }

    @Test
    void isUniqueEmailErrorTest() {
        String email = "test@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(true);

        DuplicateEmailException exception = assertThrows(DuplicateEmailException.class, () -> {
            userStorage.isUniqueEmail(email);
        });

        assertEquals("Ошибка. Такой Email уже зарегистрирован", exception.getMessage());
    }

    @Test
    void isUniqueEmailTest() {
        String email = "unique@example.com";

        when(userRepository.existsByEmail(email)).thenReturn(false);

        assertDoesNotThrow(() -> userStorage.isUniqueEmail(email));
    }
}

