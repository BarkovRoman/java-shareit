package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class ItemRequestIdResponseDto {
    Long id;
    String name;
    String description;
    Boolean available;
    Long requestId;
}
