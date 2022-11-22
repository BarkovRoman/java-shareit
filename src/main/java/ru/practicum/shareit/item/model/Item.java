package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;

@Data
@AllArgsConstructor
public class Item {
    private long id;
    private String name;
    private String description;
    private boolean available; // статус доступности
    private ItemRequest request; // ссылка на запрос создания вещи другого пользователя
}
