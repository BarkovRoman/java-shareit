package ru.practicum.shareit.item.dto;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
public class ItemDto {
    long id;
    @NotBlank
    String name;
    @NotNull
    String description;
    @NotNull
    Boolean available; // статус доступности
    long request; // ссылка на запрос создания вещи другого пользователя
}
