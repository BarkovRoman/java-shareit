package ru.practicum.shareit.user.dto;

import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class UserDto {
    Long id;
    String name;
    String email;
}
