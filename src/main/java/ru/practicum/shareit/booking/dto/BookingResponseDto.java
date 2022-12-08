package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Value
@AllArgsConstructor
public class BookingResponseDto {
    Long id;
    LocalDateTime start;
    LocalDateTime end;
    BookingStatus status;
    //Item item;
    ItemResponseDto item;
    User booker;
}
