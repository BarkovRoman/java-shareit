package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class User {

    private long id;
    private String name;

    //@Email(message = "Не верный адрес Email")
   // @NotNull(message = "Email = null")
    private String email;
}
