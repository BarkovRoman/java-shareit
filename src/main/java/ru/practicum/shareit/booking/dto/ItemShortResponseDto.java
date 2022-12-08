package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemShortResponseDto {
    ItemResponseDto toItem(Item item);

}
