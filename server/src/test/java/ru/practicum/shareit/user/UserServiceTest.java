package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.hamcrest.MatcherAssert;
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
        UserDto userDto = mapper.toUserDto(new User(0L, "Name", "User@mail.ru"));

        UserDto userDto1 = userService.add(userDto);
        Long userId = userDto1.getId();
        UserDto userDtoTest = userService.getById(userId);


        MatcherAssert.assertThat("User@mail.ru", equalTo(userDtoTest.getEmail()));
        MatcherAssert.assertThat(userId, equalTo(userDtoTest.getId()));
        assertThat(userDto1, equalTo(userDtoTest));
    }

    @Test
    public void updateUser() {
        UserDto userDto = mapper.toUserDto(new User(0L, "Name", "UserNew@mail.ru"));
        Long userId = userService.add(userDto).getId();
        UserDto userDtoUpdate = mapper.toUserDto(new User(userId, " ", " "));

        UserDto userDtoTest = userService.update(userDtoUpdate, userId);

        MatcherAssert.assertThat(userId, equalTo(userDtoTest.getId()));
        MatcherAssert.assertThat("Name", equalTo(userDtoTest.getName()));
    }

    @Test
    public void updateUserName() {
        UserDto userDto = mapper.toUserDto(new User(0L, "Name", "UserNew@mail.ru"));
        Long userId = userService.add(userDto).getId();
        UserDto userDtoUpdate = mapper.toUserDto(new User(userId, "Update", null));

        UserDto userDtoTest = userService.update(userDtoUpdate, userId);

        MatcherAssert.assertThat(userId, equalTo(userDtoTest.getId()));
        MatcherAssert.assertThat("Update", equalTo(userDtoTest.getName()));
        MatcherAssert.assertThat("UserNew@mail.ru", equalTo(userDtoTest.getEmail()));
    }

    @Test
    public void updateUserEmail() {
        UserDto userDto = mapper.toUserDto(new User(0L, "Name", "UserNew@mail.ru"));
        Long userId = userService.add(userDto).getId();
        UserDto userDtoUpdate = mapper.toUserDto(new User(userId, null, "UserUpdate@mail.ru"));

        UserDto userDtoTest = userService.update(userDtoUpdate, userId);

        MatcherAssert.assertThat(userId, equalTo(userDtoTest.getId()));
        MatcherAssert.assertThat("Name", equalTo(userDtoTest.getName()));
        MatcherAssert.assertThat("UserUpdate@mail.ru", equalTo(userDtoTest.getEmail()));
    }

    @Test
    public void getAllDeleteUser() {
        UserDto userDto = mapper.toUserDto(new User(1L, "Name", "UserNew@mail.ru"));
        UserDto userDto1 = mapper.toUserDto(new User(2L, "Name", "UserNew1@mail.ru"));
        Long userId = userService.add(userDto).getId();
        Long userId1 = userService.add(userDto1).getId();
        List<UserDto> usersTest = userService.getAll();

        assertThat(2, equalTo(usersTest.size()));

        userService.delete(userId);
        usersTest = userService.getAll();

        assertThat(1, equalTo(usersTest.size()));
        MatcherAssert.assertThat(userId1, equalTo(usersTest.get(0).getId()));
    }

    @Test
    public void userTest() {
        User user = new User(1L, "Name", "user@mail.ru");
        UserDto userDto = UserDto.builder().id(1L).name("Name").email("UserNew@mail.ru").build();

        User user1 = mapper.toUser(userDto, 1L);

        assertThat(user, equalTo(user1));
        assertThat(user.hashCode(), equalTo(user1.hashCode()));
    }
}
