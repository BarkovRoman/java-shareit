package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto add(ItemDto itemDto, long userId);

    ItemDto update(long userId, long itemId, ItemDto itemDto);

    List<ItemDto> getByUser(long userId);

    ItemDto getById(long itemId);

    List<ItemDto> search(String text);
}
