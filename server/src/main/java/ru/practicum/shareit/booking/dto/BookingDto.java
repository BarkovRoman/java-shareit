package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;

@Value
@Builder
@Jacksonized
public class BookingDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    BookingStatus status;
    Long itemId;
}
