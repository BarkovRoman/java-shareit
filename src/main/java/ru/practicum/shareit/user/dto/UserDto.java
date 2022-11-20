package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {
    private final long id;
    private final String name;

    @Email(message = "Не верный адрес Email")
    @NotNull(message = "Email = null")
    private final String email;

}
