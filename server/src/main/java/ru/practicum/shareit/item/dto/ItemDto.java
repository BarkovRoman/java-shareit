package ru.practicum.shareit.item.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    Long id;
    String name;
    String description;
    Boolean available; // статус доступности
    Long requestId;
}
