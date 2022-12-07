package ru.practicum.shareit.booking.dto;

import lombok.Value;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Value
public class BookingDto {
    Long id;
    @FutureOrPresent(message = "start не может быть раньше текущего времени ")
    LocalDateTime start;
    @Future(message = "end не может быть раньше текущего времени ")
    LocalDateTime end;
    @NotNull
    Long itemId;
    BookingStatus status;
    Long bookerId;
    String itemName;
}
