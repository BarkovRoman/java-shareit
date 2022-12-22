package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;
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
public class ItemRequestServiceTest {

    private final ItemRequestService itemRequestService;
    private final UserService userService;
    private final UserMapper userMapper;

    @Test
    public void createRequest() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.add(userDto).getId();
        UserDto userDto1 = userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"));
        Long userId1 = userService.add(userDto1).getId();

        ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1L).description("ItemRequestDescription").build();

        ItemRequestCreateDto request = itemRequestService.add(itemRequestDto, userId1);

        assertThat("ItemRequestDescription", equalTo(request.getDescription()));
    }

    @Test
    public void getByUser() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.add(userDto).getId();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1L).description("ItemRequestDescription").build();
        Long requestId = itemRequestService.add(itemRequestDto, userId).getId();

        List<ItemRequestResponseDto> requests = itemRequestService.getByUser(userId);

        assertThat(requestId, equalTo(requests.get(0).getId()));
        assertThat(1, equalTo(requests.size()));
    }

    @Test
    public void getById() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.add(userDto).getId();
        UserDto userDto1 = userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"));
        Long userId1 = userService.add(userDto1).getId();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1L).description("ItemRequestDescription").build();
        Long requestId = itemRequestService.add(itemRequestDto, userId).getId();

        ItemRequestResponseDto request = itemRequestService.getById(requestId, userId1);

        assertThat(requestId, equalTo(request.getId()));
        assertThat("ItemRequestDescription", equalTo(request.getDescription()));
    }

    @Test
    public void getAll() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.add(userDto).getId();
        UserDto userDto1 = userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"));
        Long userId1 = userService.add(userDto1).getId();
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1L).description("ItemRequestDescription").build();
        ItemRequestDto itemRequestDto1 = ItemRequestDto.builder().id(2L).description("ItemRequestDescription1").build();
        Long requestId = itemRequestService.add(itemRequestDto, userId1).getId();
        Long requestId1 = itemRequestService.add(itemRequestDto1, userId1).getId();

        List<ItemRequestResponseDto> requests = itemRequestService.getAll(0, 3, userId);

        assertThat(2, equalTo(requests.size()));
        assertThat(requestId, equalTo(requests.get(1).getId()));
        assertThat(requestId1, equalTo(requests.get(0).getId()));
    }
}
