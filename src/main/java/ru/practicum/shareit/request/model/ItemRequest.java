package ru.practicum.shareit.request;

import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private long id;
    private String description;    // текст запроса, содержащий описание требуемой вещи
    private User requestor;    // пользователь, создавший запрос

    @PastOrPresent(message = "created не может быть раньше текущего времени ")
    private LocalDateTime created;    // дата и время создания запроса.
}
