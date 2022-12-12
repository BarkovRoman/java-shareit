package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Value
@AllArgsConstructor
public class CommentDto {
    Long id;
    @NotBlank(message = "Комментарий не может быть пустым")
    String text;
    @PastOrPresent(message = "created не может быть позже текущего времени ")
    LocalDateTime created = LocalDateTime.now();
}
