package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
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
public class BookingServiceTest {

    private final UserService userService;
    private final ItemService itemService;
    private final BookingService bookingService;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    @Test
    public void createBooking() {
        UserDto userDto1 = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId1 = userService.add(userDto1).getId();
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"));
        Long userId = userService.add(userDto).getId();
        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.add(itemDto, userId1).getId();

        BookingDto bookingDto = BookingDto.builder().id(1L).itemId(itemId).status(BookingStatus.APPROVED)
                .start(LocalDateTime.now()).end(LocalDateTime.now().plusNanos(2)).build();

        BookingResponseDto booking = bookingService.add(bookingDto, userId);

        assertThat(itemId, equalTo(booking.getItem().getId()));
        assertThat(userId, equalTo(booking.getBooker().getId()));
        assertThat(BookingStatus.APPROVED, equalTo(booking.getStatus()));
    }

    @Test
    public void updateBooking() {
        UserDto userDto1 = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId1 = userService.add(userDto1).getId();
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"));
        Long userId = userService.add(userDto).getId();
        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.add(itemDto, userId1).getId();

        BookingDto bookingDto = BookingDto.builder().id(1L).itemId(itemId).status(BookingStatus.APPROVED)
                .start(LocalDateTime.now()).end(LocalDateTime.now().plusNanos(2)).build();

        long bookingId = bookingService.add(bookingDto, userId).getId();
        BookingResponseDto booking = bookingService.update(userId1, bookingId, false);

        assertThat(BookingStatus.REJECTED, equalTo(booking.getStatus()));

        booking = bookingService.update(userId1, bookingId, true);

        assertThat(BookingStatus.APPROVED, equalTo(booking.getStatus()));
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
        long bookingId = bookingService.add(bookingDto, userId).getId();

        BookingResponseDto booking = bookingService.getById(userId, bookingId);

        assertThat(userId, equalTo(booking.getBooker().getId()));
        assertThat(bookingId, equalTo(booking.getId()));
    }

    @Test
    public void getByBookerIdAndStateApproved() {
        UserDto userDto1 = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId1 = userService.add(userDto1).getId();
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"));
        Long userId = userService.add(userDto).getId();
        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.add(itemDto, userId1).getId();

        BookingDto bookingDto = BookingDto.builder().id(1L).itemId(itemId).status(BookingStatus.APPROVED)
                .start(LocalDateTime.now()).end(LocalDateTime.now().plusNanos(2)).build();
        long bookingId = bookingService.add(bookingDto, userId).getId();

        List<BookingResponseDto> bookings = bookingService.getByBookerIdAndState(userId, Status.APPROVED, 0, 1);

        assertThat(userId, equalTo(bookings.get(0).getBooker().getId()));
        assertThat(bookingId, equalTo(bookings.get(0).getId()));
        assertThat(1, equalTo(bookings.size()));
    }

    @Test
    public void getByBookerIdAndStateAll() {
        UserDto userDto1 = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId1 = userService.add(userDto1).getId();
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"));
        Long userId = userService.add(userDto).getId();
        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.add(itemDto, userId1).getId();

        BookingDto bookingDto = BookingDto.builder().id(1L).itemId(itemId).status(BookingStatus.APPROVED)
                .start(LocalDateTime.now()).end(LocalDateTime.now().plusNanos(2)).build();
        long bookingId = bookingService.add(bookingDto, userId).getId();

        List<BookingResponseDto> bookings = bookingService.getByBookerIdAndState(userId, Status.ALL, 0, 1);

        assertThat(userId, equalTo(bookings.get(0).getBooker().getId()));
        assertThat(bookingId, equalTo(bookings.get(0).getId()));
        assertThat(1, equalTo(bookings.size()));
    }

    @Test
    public void getByBookerIdAndStatePast() {
        UserDto userDto1 = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId1 = userService.add(userDto1).getId();
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"));
        Long userId = userService.add(userDto).getId();
        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.add(itemDto, userId1).getId();

        BookingDto bookingDto = BookingDto.builder().id(1L).itemId(itemId).status(BookingStatus.APPROVED)
                .start(LocalDateTime.now()).end(LocalDateTime.now().plusNanos(2)).build();
        long bookingId = bookingService.add(bookingDto, userId).getId();

        List<BookingResponseDto> bookings = bookingService.getByBookerIdAndState(userId, Status.PAST, 0, 1);

        assertThat(userId, equalTo(bookings.get(0).getBooker().getId()));
        assertThat(bookingId, equalTo(bookings.get(0).getId()));
        assertThat(1, equalTo(bookings.size()));
    }

    @Test
    public void getAllOwnerIdStatusAll() {
        UserDto userDto1 = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId1 = userService.add(userDto1).getId();
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"));
        Long userId = userService.add(userDto).getId();
        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.add(itemDto, userId1).getId();

        BookingDto bookingDto = BookingDto.builder().id(1L).itemId(itemId).status(BookingStatus.APPROVED)
                .start(LocalDateTime.now()).end(LocalDateTime.now().plusNanos(2)).build();
        long bookingId = bookingService.add(bookingDto, userId).getId();

        List<BookingResponseDto> bookings = bookingService.getAllOwnerId(userId1, Status.ALL, 0, 1);

        assertThat(userId, equalTo(bookings.get(0).getBooker().getId()));
        assertThat(bookingId, equalTo(bookings.get(0).getId()));
        assertThat(1, equalTo(bookings.size()));
    }

    @Test
    public void getAllOwnerIdStatusPast() {
        UserDto userDto1 = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId1 = userService.add(userDto1).getId();
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"));
        Long userId = userService.add(userDto).getId();
        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.add(itemDto, userId1).getId();

        BookingDto bookingDto = BookingDto.builder().id(1L).itemId(itemId).status(BookingStatus.APPROVED)
                .start(LocalDateTime.now()).end(LocalDateTime.now().plusNanos(2)).build();
        long bookingId = bookingService.add(bookingDto, userId).getId();

        List<BookingResponseDto> bookings = bookingService.getAllOwnerId(userId1, Status.PAST, 0, 1);

        assertThat(userId, equalTo(bookings.get(0).getBooker().getId()));
        assertThat(bookingId, equalTo(bookings.get(0).getId()));
        assertThat(1, equalTo(bookings.size()));
    }

    @Test
    public void getAllOwnerIdStatusApproved() {
        UserDto userDto1 = userMapper.toUserDto(new User(0L, "Name", "User@mail.ru"));
        Long userId1 = userService.add(userDto1).getId();
        UserDto userDto = userMapper.toUserDto(new User(0L, "Name", "User1@mail.ru"));
        Long userId = userService.add(userDto).getId();
        ItemDto itemDto = itemMapper.toItemDto(new Item(0L, "ItemName", "ItemDescription", true, null, userId));
        Long itemId = itemService.add(itemDto, userId1).getId();

        BookingDto bookingDto = BookingDto.builder().id(1L).itemId(itemId).status(BookingStatus.APPROVED)
                .start(LocalDateTime.now()).end(LocalDateTime.now().plusNanos(2)).build();
        long bookingId = bookingService.add(bookingDto, userId).getId();

        List<BookingResponseDto> bookings = bookingService.getAllOwnerId(userId1, Status.APPROVED, 0, 1);

        assertThat(userId, equalTo(bookings.get(0).getBooker().getId()));
        assertThat(bookingId, equalTo(bookings.get(0).getId()));
        assertThat(1, equalTo(bookings.size()));
    }












}
