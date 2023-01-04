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
    @NotBlank(message = "name NotBlank")
    String name;
    @NotBlank(message = "description NotBlank")
    String description;
    @NotNull(message = "available NotNull")
    Boolean available; // статус доступности
    Long requestId;
}
