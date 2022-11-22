package ru.practicum.shareit.user.dto;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Value
public class UserDto {
    long id;
    String name;

    @Email(message = "Не верный адрес Email")
    @NotNull(message = "Email = null")

    String email;
}
