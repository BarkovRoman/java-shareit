package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {

    private final ItemService itemService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    @Test
    public void createItem() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.add(userDto).getId();

        ItemDto itemDto = itemMapper.toItemDto(new Item(userId, "ItemName", "ItemDescription", true, null, userId));
        ItemDto itemDtoTest = itemService.add(itemDto, userId);

        assertThat(itemDto, equalTo(itemDtoTest));
    }

    @Test
    public void updateItemName() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.add(userDto).getId();

        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.add(itemDto, userId).getId();

        ItemDto itemDtoUpdate = itemMapper.toItemDto(new Item(itemId, "ItemNameUpdate", null, true, null, userId));
        ItemDto itemDtoTest = itemService.update(userId, itemId, itemDtoUpdate);

        assertThat(itemId, equalTo(itemDtoTest.getId()));
        assertThat("ItemNameUpdate", equalTo(itemDtoTest.getName()));
    }

    @Test
    public void updateItemDescription() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.add(userDto).getId();

        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.add(itemDto, userId).getId();

        ItemDto itemDtoUpdate = itemMapper.toItemDto(new Item(itemId, "ItemName", "ItemDescriptionUpdate", true, null, userId));
        ItemDto itemDtoTest = itemService.update(userId, itemId, itemDtoUpdate);

        assertThat(itemId, equalTo(itemDtoTest.getId()));
        assertThat("ItemDescriptionUpdate", equalTo(itemDtoTest.getDescription()));
    }

    @Test
    public void updateItemAvailable() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.add(userDto).getId();

        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.add(itemDto, userId).getId();

        ItemDto itemDtoUpdate = itemMapper.toItemDto(new Item(itemId, null, null, false, null, userId));
        ItemDto itemDtoTest = itemService.update(userId, itemId, itemDtoUpdate);

        assertThat(3L, equalTo(itemDtoTest.getId()));
        assertThat(false, equalTo(itemDtoTest.getAvailable()));
    }






}
