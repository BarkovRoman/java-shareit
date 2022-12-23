package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void add() {
        UserDto userDto = UserDto.builder().id(1L).name("Name").email("Name@email.ru").build();
        Mockito.when(userService.add(userDto))
                .thenReturn(userDto);

        UserDto response = userController.add(userDto);

        assertEquals("Объекты не совпадают", userDto, response);
    }

    @Test
    public void getById() {
        UserDto userDto = UserDto.builder().id(1L).name("Name").email("Name@email.ru").build();
        Mockito.when(userService.getById(1L))
                .thenReturn(userDto);

        UserDto response = userController.getById(1L);

        assertEquals("Объекты не совпадают", userDto, response);
    }

    @Test
    public void getAll() {
        UserDto userDto = UserDto.builder().id(1L).name("Name").email("Name@email.ru").build();
        List<UserDto> users = List.of(userDto);
        Mockito.when(userService.getAll())
                .thenReturn(users);

        List<UserDto> response = userController.getAll();

        assertEquals("Объекты не совпадают", users, response);
    }

    @Test
    public void update() {
        UserDto userDto = UserDto.builder().id(1L).name("Name").email("Name@email.ru").build();
        Mockito.when(userService.update(userDto, 1L))
                .thenReturn(userDto);

        UserDto response = userController.update(userDto, 1L);

        assertEquals("Объекты не совпадают", userDto, response);
    }

    @Test
    public void delete() {
        userController.delete(1L);

        Mockito.verify(userService, Mockito.times(1))
                .delete(1L);
    }
}
