package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Value;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class BookingDto {
    Long id;
    @NotNull
    @FutureOrPresent(message = "start не может быть раньше текущего времени ")
    LocalDateTime start;
    @NotNull
    @Future(message = "end не может быть раньше текущего времени ")
    LocalDateTime end;
    BookingStatus status;
    Long itemId;
}
