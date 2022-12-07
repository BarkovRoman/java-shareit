package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    ItemDto toItemDto(Item item);
    @Mapping(target = "id", source = "itemId")
    @Mapping(target = "owner", source = "userId")
    Item toItem(ItemDto itemDto, Long userId, Long itemId);
}
