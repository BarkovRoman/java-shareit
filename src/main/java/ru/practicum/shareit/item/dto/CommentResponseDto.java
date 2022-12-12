package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@AllArgsConstructor
public class CommentResponseDto {
    Long id;
    String text;
    String authorName;
    LocalDateTime created;
}
