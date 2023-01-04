package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.exception.validation.StartBeforeEndDateValid;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@StartBeforeEndDateValid
public class ItemRequestDto {
    @NotBlank
    String name;
    @NotBlank
    String description;
    @NotNull
    Boolean available; // статус доступности
    Long requestId;
}
