package ru.practicum.shareit.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class UserDto {
    Long id;

    @NotBlank(groups = {Create.class})
    String name;

    @Email(groups = {Create.class, Update.class}, message = "Не верный адрес Email")
    @NotBlank(groups = {Create.class}, message = "Email = null")
    String email;
}
