package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserRequestDto {
    Long id;

    @NotBlank(groups = {Create.class})
    String name;

    @Email(groups = {Create.class, Update.class}, message = "Не верный адрес Email")
    @NotBlank(groups = {Create.class}, message = "Email = null")
    String email;
}
