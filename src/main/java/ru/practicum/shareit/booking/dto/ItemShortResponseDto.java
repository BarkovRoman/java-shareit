package ru.practicum.shareit.booking.dto;

import org.springframework.beans.factory.annotation.Value;

public interface ItemShortResponseDto {
    @Value("#{booking.getItem().getId()}")
    Long getId();

    @Value("#{booking.getItem().getName()}")
    String getName();
}
