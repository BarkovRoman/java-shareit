package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
public class CommentDto {
    Long id;
    @NotBlank(message = "Комментарий не может быть пустым")
    String text;
}
