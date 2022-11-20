package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class User {
    private long id;
    private final String name;

    @Email(message = "Не верный адрес Email")
    @NotNull(message = "Email = null")
    private final String email;
}
