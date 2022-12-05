package ru.practicum.shareit.user.dto;

import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
public class UserDto {
    long id;

    @NotBlank(groups = {Create.class})
    String name;

    @Email(groups = {Create.class, Update.class}, message = "Не верный адрес Email")
    @NotNull(groups = {Create.class}, message = "Email = null")
    String email;
}
