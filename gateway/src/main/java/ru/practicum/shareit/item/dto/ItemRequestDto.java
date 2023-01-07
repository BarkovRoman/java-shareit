package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ItemRequestDto {
    @NotBlank(message = "name NotBlank")
    String name;
    @NotBlank(message = "description NotBlank")
    String description;
    @NotNull(message = "available NotNull")
    Boolean available; // статус доступности
    Long requestId;
}