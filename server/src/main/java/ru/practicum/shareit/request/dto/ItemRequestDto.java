package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ItemRequestDto {
    long id;
    String description;
}
