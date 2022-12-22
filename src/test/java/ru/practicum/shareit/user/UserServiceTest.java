package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {

    private final UserService userService;
    private final UserMapper mapper;

    @Test
    public void createUser() {
        UserDto userDto = mapper.toUserDto(new User(4L, "Name", "User@mail.ru"));

        userService.add(userDto);
        UserDto userDtoTest = userService.getById(4L);

        assertThat("User@mail.ru", equalTo(userDtoTest.getEmail()));
        assertThat(4L, equalTo(userDtoTest.getId()));
    }

    @Test
    public void updateUserName() {
        UserDto userDto = mapper.toUserDto(new User(3L, "Name", "UserNew@mail.ru"));
        UserDto userDtoUpdate = mapper.toUserDto(new User(3L, "Update", "UserNew@mail.ru"));

        userService.add(userDto);
        userService.update(userDtoUpdate, 3L);

        UserDto userDtoTest = userService.getById(3L);

        assertThat(3L, equalTo(userDtoTest.getId()));
        assertThat("Update", equalTo(userDtoTest.getName()));
        assertThat("UserNew@mail.ru", equalTo(userDtoTest.getEmail()));
    }

    @Test
    public void updateUserEmail() {
        UserDto userDto = mapper.toUserDto(new User(5L, "Name", "UserNew@mail.ru"));
        UserDto userDtoUpdate = mapper.toUserDto(new User(5L, "Name", "UserUpdate@mail.ru"));

        userService.add(userDto);
        userService.update(userDtoUpdate, 5L);

        UserDto userDtoTest = userService.getById(5L);

        assertThat(5L, equalTo(userDtoTest.getId()));
        assertThat("Name", equalTo(userDtoTest.getName()));
        assertThat("UserUpdate@mail.ru", equalTo(userDtoTest.getEmail()));
    }

    @Test
    public void getAllDeleteUser() {
        UserDto userDto = mapper.toUserDto(new User(1L, "Name", "UserNew@mail.ru"));
        UserDto userDto1 = mapper.toUserDto(new User(2L, "Name", "UserNew1@mail.ru"));
        List<UserDto> users = List.of(userDto, userDto1);

        userService.add(userDto);
        userService.add(userDto1);

        List<UserDto> usersTest = userService.getAll();

        assertThat(users, equalTo(usersTest));
        assertThat(2, equalTo(usersTest.size()));

        userService.delete(1L);
        usersTest = userService.getAll();

        assertThat(1, equalTo(usersTest.size()));
        assertThat(2L, equalTo(usersTest.get(0).getId()));
    }
}