package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.ExistingValidationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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
    private final BookingService bookingService;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    @Test
    @Sql("/dataTest.sql")
    public void createItem() {
        Long userId = 1L;

        ItemDto itemDto = itemMapper.toItemDto(new Item(userId, "ItemName", "ItemDescription", true, null, userId));
        ItemDto itemDtoTest = itemService.add(itemDto, userId);

        assertThat("ItemName", equalTo(itemDtoTest.getName()));
        assertThat("ItemDescription", equalTo(itemDtoTest.getDescription()));
    }

    @Test
    @Sql("/dataTest.sql")
    public void updateItemName() {
        Long userId = 1L;
        Long itemId = 1L;

        ItemDto itemDtoUpdate = itemMapper.toItemDto(new Item(itemId, "ItemNameUpdate", null, true, null, userId));
        ItemDto itemDtoTest = itemService.update(userId, itemId, itemDtoUpdate);

        assertThat(itemId, equalTo(itemDtoTest.getId()));
        assertThat("ItemNameUpdate", equalTo(itemDtoTest.getName()));
    }

    @Test
    @Sql("/dataTest.sql")
    public void updateItemDescription() {
        Long userId = 1L;
        Long itemId = 1L;

        ItemDto itemDtoUpdate = itemMapper.toItemDto(new Item(itemId, "ItemName", "ItemDescriptionUpdate", true, null, userId));
        ItemDto itemDtoTest = itemService.update(userId, itemId, itemDtoUpdate);

        assertThat(itemId, equalTo(itemDtoTest.getId()));
        assertThat("ItemDescriptionUpdate", equalTo(itemDtoTest.getDescription()));
    }

    @Test
    @Sql("/dataTest.sql")
    public void updateItemAvailable() {
        Long userId = 1L;
        Long itemId = 1L;

        ItemDto itemDtoUpdate = itemMapper.toItemDto(new Item(itemId, null, null, false, null, userId));
        ItemDto itemDtoTest = itemService.update(userId, itemId, itemDtoUpdate);

        assertThat(itemId, equalTo(itemDtoTest.getId()));
        assertThat(false, equalTo(itemDtoTest.getAvailable()));
    }

    @Test
    public void updateItemNotUserId() {
        Long userId = 1L;
        Long itemId = 1L;

        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));

        assertThatThrownBy(() -> {
            itemService.update(50L, itemId, itemDto);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    @Sql("/dataTest.sql")
    public void getByUser() {
        Long userId = 1L;
        Long itemId = 1L;
        String name = "item1";

        List<ItemBookingDto> items = itemService.getByUser(userId, 0, 1);

        assertThat(1, equalTo(items.size()));
        assertThat(itemId, equalTo(items.get(0).getId()));
        assertThat(name, equalTo(items.get(0).getName()));
    }

    @Test
    @Sql("/dataTest.sql")
    public void getByUserByBooking() {
        Long userId = 1L;
        Long itemId = 1L;
        String name = "item1";

        List<ItemBookingDto> items = itemService.getByUser(userId, 0, 2);

        assertThat(1, equalTo(items.size()));
        assertThat(itemId, equalTo(items.get(0).getId()));
        assertThat(name, equalTo(items.get(0).getName()));
    }

    @Test
    @Sql("/dataTest.sql")
    public void getById() {
        Long userId = 1L;
        Long itemId = 1L;
        String name = "item1";
        String description = "descriptionItem1";

        ItemBookingDto itemTest = itemService.getById(itemId, userId);

        assertThat(itemId, equalTo(itemTest.getId()));
        assertThat(name, equalTo(itemTest.getName()));
        assertThat(description, equalTo(itemTest.getDescription()));
    }

    @Test
    @Sql("/dataTest.sql")
    public void search() {
        Long itemId = 1L;
        String name = "item1";
        String text = "1";

        List<ItemDto> items = itemService.search(text, 0, 1);

        assertThat(1, equalTo(items.size()));
        assertThat(name, equalTo(items.get(0).getName()));
        assertThat(itemId, equalTo(items.get(0).getId()));
    }

    @Test
    public void createComments() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.add(userDto).getId();
        Long userId1 = userService.add(userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"))).getId();

        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.add(itemDto, userId).getId();

        BookingDto bookingDto = BookingDto.builder().id(1L).itemId(itemId).status(BookingStatus.APPROVED)
                .start(LocalDateTime.now()).end(LocalDateTime.now().plusNanos(2)).build();
        bookingService.add(bookingDto, userId1);
        CommentDto commentDto = CommentDto.builder().id(1L).text("Comments").build();

        CommentResponseDto comment = itemService.addComments(userId1, itemId, commentDto);

        assertThat(1L, equalTo(comment.getId()));
        assertThat("Name", equalTo(comment.getAuthorName()));
    }

    @Test
    public void itemTest() {
        User user = new User(1L, "Name", "user@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1L, "Description", user, LocalDateTime.now());
        Item item = new Item(1L, "Name", "Description", true, itemRequest, 1L);
        ItemDto itemDto = ItemDto.builder().id(1L).name("Name").description("Description").available(true).requestId(1L).build();

        Item item1 = itemMapper.toItem(itemDto, 1L, itemRequest);

        assertThat(item, equalTo(item1));
        assertThat(item.hashCode(), equalTo(item1.hashCode()));
    }

    @Test
    public void commentTest() {
        User user = new User(1L, "Name", "user@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1L, "Description", user, LocalDateTime.now());
        Item item = new Item(1L, "Name", "Description", true, itemRequest, 1L);
        Comment comment = new Comment(1L, "Text", item, user, LocalDateTime.now());
        CommentDto commentDto = CommentDto.builder().id(1L).text("Text").build();

        Comment comment1 = itemMapper.toComment(commentDto, item, user);
        comment1.setId(1L);

        assertThat(comment, equalTo(comment1));
        assertThat(comment.hashCode(), equalTo(comment1.hashCode()));
    }

    @Test
    @Sql("/dataTest.sql")
    public void getByUserByComment() {
        Long userId = 1L;
        Long itemId = 1L;
        String name = "item1";

        List<ItemBookingDto> items = itemService.getByUser(userId, 0, 1);

        assertThat(1, equalTo(items.size()));
        assertThat(itemId, equalTo(items.get(0).getId()));
        assertThat(name, equalTo(items.get(0).getName()));
    }

    @Test
    @Sql("/dataTest.sql")
    public void getByIdByComment() {
        Long userId = 1L;
        Long itemId = 1L;
        String name = "item1";

        ItemBookingDto item = itemService.getById(itemId, userId);

        assertThat(itemId, equalTo(item.getId()));
        assertThat(name, equalTo(item.getName()));
    }

    @Test
    @Sql("/dataTest.sql")
    public void updateItemIsBlank() {
        Long userId = 1L;
        Long itemId = 1L;

        ItemDto itemDtoUpdate = itemMapper.toItemDto(new Item(itemId, " ", " ", null, null, userId));
        ItemDto itemDtoTest = itemService.update(userId, itemId, itemDtoUpdate);

        assertThat(itemId, equalTo(itemDtoTest.getId()));
        assertThat(true, equalTo(itemDtoTest.getAvailable()));
    }

    @Test
    @Sql("/dataTest.sql")
    public void searchPage() {
        Long itemId = 2L;

        List<ItemDto> items = itemService.search("descrip", 1, 1);

        assertThat(1, equalTo(items.size()));
        assertThat(itemId, equalTo(items.get(0).getId()));
    }

    @Test
    @Sql("/dataTest.sql")
    public void createCommentsError() {
        Long userId1 = 1L;
        Long itemId = 1L;

        assertThatThrownBy(() -> {
            CommentResponseDto comment = itemService.addComments(userId1, itemId, CommentDto.builder().build());
        }).isInstanceOf(ExistingValidationException.class);
    }
}
