package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
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
    private final ItemRequestMapper itemRequestMapper;
    private final ItemService itemService;
    private final ItemMapper itemMapper;

    @Test
    public void createRequest() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.add(userDto).getId();
        UserDto userRequestDto1 = userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"));
        Long userId1 = userService.add(userRequestDto1).getId();

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

    @Test
    public void itemRequestTest() {
        User user = new User(1L, "Name", "user@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1L,"Description", user, LocalDateTime.now());
        ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1L).description("Description").build();

        ItemRequest itemRequest1 = itemRequestMapper.toItemRequest(itemRequestDto, user);

        assertThat(itemRequest, equalTo(itemRequest1));
        assertThat(itemRequest.hashCode(), equalTo(itemRequest1.hashCode()));
    }

    @Test
    public void getAll1() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.add(userDto).getId();
        UserDto userDto1 = userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"));
        Long userId1 = userService.add(userDto1).getId();

        ItemRequestDto itemRequestDto = ItemRequestDto.builder().id(1L).description("ItemRequestDescription").build();
        ItemRequestDto itemRequestDto1 = ItemRequestDto.builder().id(2L).description("ItemRequestDescription1").build();
        Long requestId = itemRequestService.add(itemRequestDto, userId1).getId();
        Long requestId1 = itemRequestService.add(itemRequestDto1, userId1).getId();
        ItemRequest itemRequest = itemRequestMapper.toItemRequest(itemRequestDto, new User(userId1, "Name", "User@mail.ru"));
        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, itemRequest, userId));
        Long itemId = itemService.add(itemDto, userId).getId();

        List<ItemRequestResponseDto> requests = itemRequestService.getAll(0, 3, userId);

        assertThat(2, equalTo(requests.size()));
        assertThat(requestId, equalTo(requests.get(1).getId()));
        assertThat(requestId1, equalTo(requests.get(0).getId()));
    }
}
