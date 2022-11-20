package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto addItem(ItemDto item, long userId);
    ItemDto update(long userId, long itemId, ItemDto itemDto);
    List<ItemDto> getItems(long userId);
    ItemDto getBuId(long itemId);
    List<ItemDto> searchItem(String text);
}
