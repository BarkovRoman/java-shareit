package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.dto.UserRequestDto;
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
        UserRequestDto userRequestDto = mapper.toUserDto(new User(0L, "Name", "User@mail.ru"));

        UserRequestDto userRequestDto1 = userService.add(userRequestDto);
        Long userId = userRequestDto1.getId();
        UserRequestDto userRequestDtoTest = userService.getById(userId);


        MatcherAssert.assertThat("User@mail.ru", equalTo(userRequestDtoTest.getEmail()));
        MatcherAssert.assertThat(userId, equalTo(userRequestDtoTest.getId()));
        assertThat(userRequestDto1, equalTo(userRequestDtoTest));
    }

    @Test
    public void updateUser() {
        UserRequestDto userRequestDto = mapper.toUserDto(new User(0L, "Name", "UserNew@mail.ru"));
        Long userId = userService.add(userRequestDto).getId();
        UserRequestDto userRequestDtoUpdate = mapper.toUserDto(new User(userId, " ", " "));

        UserRequestDto userRequestDtoTest = userService.update(userRequestDtoUpdate, userId);

        MatcherAssert.assertThat(userId, equalTo(userRequestDtoTest.getId()));
        MatcherAssert.assertThat("Name", equalTo(userRequestDtoTest.getName()));
    }

    @Test
    public void updateUserName() {
        UserRequestDto userRequestDto = mapper.toUserDto(new User(0L, "Name", "UserNew@mail.ru"));
        Long userId = userService.add(userRequestDto).getId();
        UserRequestDto userRequestDtoUpdate = mapper.toUserDto(new User(userId, "Update", null));

        UserRequestDto userRequestDtoTest = userService.update(userRequestDtoUpdate, userId);

        MatcherAssert.assertThat(userId, equalTo(userRequestDtoTest.getId()));
        MatcherAssert.assertThat("Update", equalTo(userRequestDtoTest.getName()));
        MatcherAssert.assertThat("UserNew@mail.ru", equalTo(userRequestDtoTest.getEmail()));
    }

    @Test
    public void updateUserEmail() {
        UserRequestDto userRequestDto = mapper.toUserDto(new User(0L, "Name", "UserNew@mail.ru"));
        Long userId = userService.add(userRequestDto).getId();
        UserRequestDto userRequestDtoUpdate = mapper.toUserDto(new User(userId, null, "UserUpdate@mail.ru"));

        UserRequestDto userRequestDtoTest = userService.update(userRequestDtoUpdate, userId);

        MatcherAssert.assertThat(userId, equalTo(userRequestDtoTest.getId()));
        MatcherAssert.assertThat("Name", equalTo(userRequestDtoTest.getName()));
        MatcherAssert.assertThat("UserUpdate@mail.ru", equalTo(userRequestDtoTest.getEmail()));
    }

    @Test
    public void getAllDeleteUser() {
        UserRequestDto userRequestDto = mapper.toUserDto(new User(1L, "Name", "UserNew@mail.ru"));
        UserRequestDto userRequestDto1 = mapper.toUserDto(new User(2L, "Name", "UserNew1@mail.ru"));
        Long userId = userService.add(userRequestDto).getId();
        Long userId1 = userService.add(userRequestDto1).getId();
        List<UserRequestDto> usersTest = userService.getAll();

        assertThat(2, equalTo(usersTest.size()));

        userService.delete(userId);
        usersTest = userService.getAll();

        assertThat(1, equalTo(usersTest.size()));
        MatcherAssert.assertThat(userId1, equalTo(usersTest.get(0).getId()));
    }

    @Test
    public void userTest() {
        User user = new User(1L, "Name", "user@mail.ru");
        UserRequestDto userRequestDto = UserRequestDto.builder().id(1L).name("Name").email("UserNew@mail.ru").build();

        User user1 = mapper.toUser(userRequestDto, 1L);

        assertThat(user, equalTo(user1));
        assertThat(user.hashCode(), equalTo(user1.hashCode()));
    }
}