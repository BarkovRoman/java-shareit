package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookerResponseDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @Test
    public void add() {
        BookingDto bookingDto = BookingDto.builder()
                .id(1L).itemId(1L).status(BookingStatus.APPROVED)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusNanos(2))
                .build();
        BookingResponseDto booking = new BookingResponseDto(
                1L, LocalDateTime.now(),
                LocalDateTime.now().plusNanos(2),
                BookingStatus.APPROVED,
                new ItemResponseDto(1L, "Name"),
                new BookerResponseDto(1L));
        Mockito
                .when(bookingService.add(bookingDto, 1L))
                .thenReturn(booking);

        BookingResponseDto response = bookingController.add(1L, bookingDto);
        assertEquals("Объекты не совпадают", booking, response);
        assertEquals("Ошибка Id не совпадают", bookingDto.getId(), response.getId());
        assertEquals("Ошибка Id Item не совпадают", bookingDto.getItemId(), response.getItem().getId());
    }

    @Test
    public void update() {
        BookingDto bookingDto = BookingDto.builder()
                .id(1L).itemId(1L).status(BookingStatus.REJECTED)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now().plusNanos(2))
                .build();
        BookingResponseDto booking = new BookingResponseDto(
                1L, LocalDateTime.now(),
                LocalDateTime.now().plusNanos(2),
                BookingStatus.APPROVED,
                new ItemResponseDto(1L, "Name"),
                new BookerResponseDto(1L));
        Mockito
                .when(bookingService.update(1L, 1L, true))
                .thenReturn(booking);

        BookingResponseDto response = bookingController.update(1L, true, 1L);

        assertEquals("Ошибка Id не совпадают", bookingDto.getId(), response.getId());
        assertEquals("Ошибка Status не изменен", BookingStatus.APPROVED, response.getStatus());
    }

    @Test
    public void getById() {
        BookingResponseDto booking = new BookingResponseDto(
                1L, LocalDateTime.now(),
                LocalDateTime.now().plusNanos(2),
                BookingStatus.APPROVED,
                new ItemResponseDto(1L, "Name"),
                new BookerResponseDto(1L));
        Mockito
                .when(bookingService.getById(1L, 1L))
                .thenReturn(booking);

        BookingResponseDto response = bookingController.getById(1L, 1L);

        assertEquals("Ошибка Id не совпадают", booking, response);
        assertEquals("Ошибка Status не изменен", BookingStatus.APPROVED, response.getStatus());
    }

    @Test
    public void getByBookerIdAndState() {
        BookingResponseDto booking = new BookingResponseDto(
                1L, LocalDateTime.now(),
                LocalDateTime.now().plusNanos(2),
                BookingStatus.APPROVED,
                new ItemResponseDto(1L, "Name"),
                new BookerResponseDto(1L));
        List<BookingResponseDto> bookings = List.of(booking);
        Mockito
                .when(bookingService.getByBookerIdAndState(1L, Status.ALL, 0, 5))
                .thenReturn(bookings);

        List<BookingResponseDto> response = bookingController.getByBookerIdAndState(1L, Status.ALL, 0, 5);

        assertEquals("Ошибка Id не совпадают", bookings, response);
    }

    @Test
    public void getAllOwnerId() {
        BookingResponseDto booking = new BookingResponseDto(
                1L, LocalDateTime.now(),
                LocalDateTime.now().plusNanos(2),
                BookingStatus.APPROVED,
                new ItemResponseDto(1L, "Name"),
                new BookerResponseDto(1L));
        List<BookingResponseDto> bookings = List.of(booking);
        Mockito
                .when(bookingService.getAllOwnerId(1L, Status.ALL, 0, 5))
                .thenReturn(bookings);

        List<BookingResponseDto> response = bookingController.getAllOwnerId(1L, Status.ALL, 0, 5);

        assertEquals("Ошибка Id не совпадают", bookings, response);
    }
}
