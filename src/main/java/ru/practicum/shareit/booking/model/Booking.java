package ru.practicum.shareit.booking.model;

import lombok.Data;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
public class Booking {
    private long id;

    @PastOrPresent(message = "start не может быть раньше текущего времени ")
    private LocalDateTime start;    // Дата и время начала бронирования

    @PastOrPresent(message = "end не может быть раньше текущего времени ")
    private LocalDateTime end;    // Дата и время конца бронирования;
    private Item item;
    private User booker;    // Пользователь, который осуществляет бронирование
    private BookingStatus status;
}
