package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.shareit.user.model.User;

@Value
@Builder
@Jacksonized
public class ItemRequestDto {
    long id;
    String description;    // текст запроса, содержащий описание требуемой вещи
    User requestor;    // пользователь, создавший запрос
}
