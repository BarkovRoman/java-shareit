package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "id", source = "itemId")
    @Mapping(target = "owner", source = "userId")
    Item toItem(ItemDto itemDto, Long userId, Long itemId);

    ItemDto toItemDto(Item item);
    @Mapping(target = "id", source = "id")
    @Mapping(target = "lastBooking", source = "lastBooking")
    @Mapping(target = "nextBooking", source = "nextBooking")
    ItemBookingDto toItemBookingDto(Item item, LastNextItemShortDto lastBooking, LastNextItemShortDto nextBooking, Long id);

}
