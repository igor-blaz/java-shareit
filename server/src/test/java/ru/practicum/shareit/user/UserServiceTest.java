package ru.practicum.shareit.user;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserRequestDto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserStorage userStorage;

    private UserRequestDto userRequestDto;
    private User user;
    private UserDto expectedUserDto;

    @BeforeEach
    void setUp() {
        userRequestDto = new UserRequestDto("Igor", "igor@email.com");
        user = new User(1L, "Igor", "igor@email.com");
        expectedUserDto = new UserDto(1L, "Igor", "igor@email.com");
    }

    @Test
    void addUserTest() {
        User savedUser = new User(1L, "Igor", "igor@email.com");
        when(userStorage.addUser(any(User.class))).thenReturn(savedUser);


        UserDto actualUserDto = userService.addUser(userRequestDto);

        assertEquals(expectedUserDto.getId(), actualUserDto.getId());
        assertEquals(expectedUserDto.getName(), actualUserDto.getName());
        assertEquals(expectedUserDto.getEmail(), actualUserDto.getEmail());

        verify(userStorage, times(1)).addUser(any(User.class));
    }

    @Test
    void getUserDtoTest() {
        when(userStorage.getUserById(anyLong())).thenReturn(user);

        UserDto actualUserDto = userService.getUserDto(1L);

        assertEquals(expectedUserDto.getId(), actualUserDto.getId());
        assertEquals(expectedUserDto.getName(), actualUserDto.getName());
        assertEquals(expectedUserDto.getEmail(), actualUserDto.getEmail());

        verify(userStorage, times(1)).getUserById(1L);

    }

    @Test
    void deleteUserTest() {
        userService.deleteUser(1L);
        verify(userStorage, times(1)).deleteUser(1L);
    }

    @Test
    void updateUserTest() {
        when(userStorage.getUserById(anyLong())).thenReturn(user);
        User newUser = Instancio.create(User.class);
        newUser.setId(1L);
        UserDto dto = userService.updateUser(1L, newUser);
        verify(userStorage, times(1)).getUserById(1L);
        assertThat(dto.getName()).isEqualTo(newUser.getName());
        assertThat(dto.getEmail()).isEqualTo(newUser.getEmail());
    }

    @Test
    void getUserTest() {
        when(userStorage.getUserById(anyLong())).thenReturn(user);

        User actualUser = userService.getUser(1L);
        User expectedUser = UserMapper.userDtoConverter(expectedUserDto);
        assertEquals(expectedUser.getId(), actualUser.getId());
        assertEquals(expectedUser.getName(), actualUser.getName());
        assertEquals(expectedUser.getEmail(), actualUser.getEmail());

        verify(userStorage, times(1)).getUserById(1L);

    }
}

