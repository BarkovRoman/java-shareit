package ru.practicum.shareit.item.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ItemDto {
    Long id;
    String name;
    String description;
    Boolean available; // статус доступности
    Long requestId;
}
