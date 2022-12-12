package ru.practicum.shareit.item.dto;

import org.springframework.beans.factory.annotation.Value;

public interface LastNextItemShortDto {
    Long getId();    // Дата и время начала бронирования

    @Value("#{target.booker.id}")

    Long getBookerId();
}
