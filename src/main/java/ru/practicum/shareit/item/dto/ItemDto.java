package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ItemDto {
    private String name;
    private String description;
    private boolean available; // статус доступности
    private long request; // ссылка на запрос создания вещи другого пользователя
}
