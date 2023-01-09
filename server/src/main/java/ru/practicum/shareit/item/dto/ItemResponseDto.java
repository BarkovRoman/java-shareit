package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ItemResponseDto {
    Long id;
    String name;
}
