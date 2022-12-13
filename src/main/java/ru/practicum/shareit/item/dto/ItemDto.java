package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class ItemDto {
    Long id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    @NotNull
    Boolean available; // статус доступности
}
