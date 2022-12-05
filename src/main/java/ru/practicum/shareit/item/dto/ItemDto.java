package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@AllArgsConstructor
public class ItemDto {
    long id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    @NotNull
    Boolean available; // статус доступности
    //long request; // ссылка на запрос создания вещи другого пользователя
    long owner;
}
