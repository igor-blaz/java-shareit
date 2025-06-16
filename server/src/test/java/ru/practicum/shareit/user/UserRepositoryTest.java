package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {


    @Autowired
    private UserRepository userRepository;

    @Test
    void saveUser() {
        User user = new User(null, "Igor", "igor@email.com");
        User saved = userRepository.save(user);
        Optional<User> foundUser = userRepository.findById(saved.getId());
        assertTrue(foundUser.isPresent());
        assertEquals("Igor", foundUser.get().getName());
    }



    @Test
    void testDeleteById() {
        User user = userRepository.save(new User(null, "Igor", "igor@email.com"));
        userRepository.deleteById(user.getId());
        Optional<User> deleted = userRepository.findById(user.getId());
        assertFalse(deleted.isPresent());
    }
}
