package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
    public void createItem() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.add(userDto).getId();

        ItemDto itemDto = itemMapper.toItemDto(new Item(userId, "ItemName", "ItemDescription", true, null, userId));
        ItemDto itemDtoTest = itemService.add(itemDto, userId);

        assertThat("ItemName", equalTo(itemDtoTest.getName()));
        assertThat("ItemDescription", equalTo(itemDtoTest.getDescription()));
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

        assertThat(itemId, equalTo(itemDtoTest.getId()));
        assertThat(false, equalTo(itemDtoTest.getAvailable()));
    }

    @Test
    public void updateItemNotUserId() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.add(userDto).getId();
        UserDto userDto1 = userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"));
        Long userId1 = userService.add(userDto1).getId();

        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.add(itemDto, userId).getId();

        assertThatThrownBy(() -> {
            ItemDto itemDtoTest = itemService.update(userId1, itemId, itemDto);
        }).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void getByUser() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.add(userDto).getId();

        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.add(itemDto, userId).getId();

        List<ItemBookingDto> items = itemService.getByUser(userId, 0, 1);

        assertThat(1, equalTo(items.size()));
        assertThat(itemId, equalTo(items.get(0).getId()));
        assertThat("ItemName", equalTo(items.get(0).getName()));
    }

    @Test
    public void getByUserByBooking() {
        UserDto userDto1 = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId1 = userService.add(userDto1).getId();
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"));
        Long userId = userService.add(userDto).getId();
        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.add(itemDto, userId1).getId();

        BookingDto bookingDto = BookingDto.builder().id(1L).itemId(itemId).status(BookingStatus.APPROVED)
                .start(LocalDateTime.now()).end(LocalDateTime.now().plusNanos(2)).build();
        long bookingId = bookingService.add(bookingDto, userId).getId();

        List<ItemBookingDto> items = itemService.getByUser(userId1, 0, 2);

        assertThat(1, equalTo(items.size()));
        assertThat(itemId, equalTo(items.get(0).getId()));
        assertThat("ItemName", equalTo(items.get(0).getName()));
    }

    @Test
    public void getById() {
        UserDto userDto1 = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId1 = userService.add(userDto1).getId();
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"));
        Long userId = userService.add(userDto).getId();
        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.add(itemDto, userId1).getId();

        BookingDto bookingDto = BookingDto.builder().id(1L).itemId(itemId).status(BookingStatus.APPROVED)
                .start(LocalDateTime.now()).end(LocalDateTime.now().plusNanos(2)).build();
        bookingService.add(bookingDto, userId);

        ItemBookingDto itemTest = itemService.getById(itemId, userId1);

        assertThat(itemId, equalTo(itemTest.getId()));
        assertThat("ItemName", equalTo(itemTest.getName()));
        assertThat("ItemDescription", equalTo(itemTest.getDescription()));
    }

    @Test
    public void search() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.add(userDto).getId();

        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.add(itemDto, userId).getId();

        List<ItemDto> items = itemService.search("descrip", 0, 1);

        assertThat(1, equalTo(items.size()));
        assertThat("ItemName", equalTo(items.get(0).getName()));
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
        ItemRequest itemRequest = new ItemRequest(1L,"Description", user, LocalDateTime.now());
        Item item = new Item(1L, "Name", "Description", true, itemRequest, 1L);
        ItemDto itemDto = ItemDto.builder().id(1L).name("Name").description("Description").available(true).requestId(1L).build();

        Item item1 = itemMapper.toItem(itemDto, 1L, itemRequest);

        assertThat(item, equalTo(item1));
        assertThat(item.hashCode(), equalTo(item1.hashCode()));
    }

    @Test
    public void commentTest() {
        User user = new User(1L, "Name", "user@mail.ru");
        ItemRequest itemRequest = new ItemRequest(1L,"Description", user, LocalDateTime.now());
        Item item = new Item(1L, "Name", "Description", true, itemRequest, 1L);
        Comment comment = new Comment(1L, "Text", item, user, LocalDateTime.now());
        CommentDto commentDto = CommentDto.builder().id(1L).text("Text").build();

        Comment comment1 = itemMapper.toComment(commentDto, item, user);
        comment1.setId(1L);

        assertThat(comment, equalTo(comment1));
        assertThat(comment.hashCode(), equalTo(comment1.hashCode()));
    }

    @Test
    public void getByUserByComment() {
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

        BookingDto bookingDto1 = BookingDto.builder().id(2L).itemId(itemId).status(BookingStatus.APPROVED)
                .start(LocalDateTime.now().plusNanos(10)).end(LocalDateTime.now().plusNanos(20)).build();
        bookingService.add(bookingDto1, userId1);

        List<ItemBookingDto> items = itemService.getByUser(userId, 0, 1);

        assertThat(1, equalTo(items.size()));
        assertThat(itemId, equalTo(items.get(0).getId()));
        assertThat("ItemName", equalTo(items.get(0).getName()));
    }

    @Test
    public void getByIdByComment() {
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

        BookingDto bookingDto1 = BookingDto.builder().id(2L).itemId(itemId).status(BookingStatus.APPROVED)
                .start(LocalDateTime.now().plusNanos(10)).end(LocalDateTime.now().plusNanos(20)).build();
        bookingService.add(bookingDto1, userId1);

        ItemBookingDto item = itemService.getById(itemId, userId);

        assertThat(itemId, equalTo(item.getId()));
        assertThat("ItemName", equalTo(item.getName()));
    }

    @Test
    public void updateItemIsBlank() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.add(userDto).getId();

        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.add(itemDto, userId).getId();

        ItemDto itemDtoUpdate = itemMapper.toItemDto(new Item(itemId, " ", " ", null, null, userId));
        ItemDto itemDtoTest = itemService.update(userId, itemId, itemDtoUpdate);

        assertThat(itemId, equalTo(itemDtoTest.getId()));
        assertThat(true, equalTo(itemDtoTest.getAvailable()));
    }

    @Test
    public void searchPage() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.add(userDto).getId();

        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.add(itemDto, userId).getId();

        ItemDto itemDto1 = itemMapper.toItemDto(new Item(0L, "ItemName", "Description", true, null, userId));
        Long itemId1 = itemService.add(itemDto1, userId).getId();

        ItemDto itemDto3 = itemMapper.toItemDto(new Item(0L, "Description", "ItemDescription", true, null, userId));
        Long itemId3 = itemService.add(itemDto3, userId).getId();

        List<ItemDto> items = itemService.search("descrip", 1, 1);

        assertThat(1, equalTo(items.size()));
        assertThat(itemId1, equalTo(items.get(0).getId()));
    }

    @Test
    public void createCommentsError() {
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId = userService.add(userDto).getId();
        Long userId1 = userService.add(userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"))).getId();

        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.add(itemDto, userId).getId();

        BookingDto bookingDto = BookingDto.builder().id(1L).itemId(itemId).status(BookingStatus.REJECTED)
                .start(LocalDateTime.now()).end(LocalDateTime.now().plusNanos(2)).build();
        bookingService.add(bookingDto, userId1);
        CommentDto commentDto = CommentDto.builder().id(1L).text("Comments").build();

        assertThatThrownBy(() -> {
            CommentResponseDto comment = itemService.addComments(userId1, itemId, commentDto);
        }).isInstanceOf(ExistingValidationException.class);
    }
}
