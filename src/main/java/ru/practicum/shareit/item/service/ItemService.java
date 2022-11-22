package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemDto itemDto, long userId);
    ItemDto update(long userId, long itemId, ItemDto itemDto);
    List<ItemDto> getItems(long userId);
    ItemDto getBuId(long itemId);
    List<ItemDto> searchItem(String text);
}
