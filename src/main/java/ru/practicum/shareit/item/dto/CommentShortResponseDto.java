package ru.practicum.shareit.item.dto;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

public interface CommentShortResponseDto {
    Long getId();
    String getText();
    @Value("#{target.author.name}")
    String getName();
    LocalDateTime getCreated();
}
