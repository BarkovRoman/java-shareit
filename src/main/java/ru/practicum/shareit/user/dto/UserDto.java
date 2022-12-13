package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder
@Jacksonized
public class UserDto {
    Long id;

    @NotBlank(groups = {Create.class})
    String name;

    @Email(groups = {Create.class, Update.class}, message = "Не верный адрес Email")
    @NotNull(groups = {Create.class}, message = "Email = null")
    String email;
}
