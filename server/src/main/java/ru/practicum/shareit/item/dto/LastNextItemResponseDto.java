package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class LastNextItemResponseDto {
    Long id;
    Long bookerId;
}
