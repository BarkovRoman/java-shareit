package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ItemDto {
    private final long id;
    @NotNull
    private final String name;
    @NotNull
    private final String description;
    @NotNull
    private final Boolean available; // статус доступности
    private final long request; // ссылка на запрос создания вещи другого пользователя
}
