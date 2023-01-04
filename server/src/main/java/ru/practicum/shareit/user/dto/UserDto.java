package ru.practicum.shareit.user.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    Long id;
    String name;
    String email;
}
