package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.request.ItemRequest;

@Data
//@AllArgsConstructor
public class Item {
    private long id;
    private final String name;
    private final String description;
    private final boolean available; // статус доступности
    private final ItemRequest request; // ссылка на запрос создания вещи другого пользователя

   /* public Item(String name, String description, boolean available, ItemRequest request) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.request = request;
    }*/
}
