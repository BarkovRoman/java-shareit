package ru.practicum.shareit.booking.dto;

import lombok.Value;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Value
public class BookingDto {
    Long id;
    @PastOrPresent(message = "start не может быть раньше текущего времени ")
    LocalDateTime start;
    @PastOrPresent(message = "end не может быть раньше текущего времени ")
    LocalDateTime end;
    BookingStatus status;
}
