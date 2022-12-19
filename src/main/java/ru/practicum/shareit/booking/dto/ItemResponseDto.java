package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ItemResponseDto {
    Long id;
    String name;
}
