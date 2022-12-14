package ru.practicum.shareit.booking.dto;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.validation.StartBeforeEndDateValid;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Value
@StartBeforeEndDateValid
@Builder
@Jacksonized
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
