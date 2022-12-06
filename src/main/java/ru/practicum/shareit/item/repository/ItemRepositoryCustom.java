package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

public interface ItemRepositoryCustom {
    Item update(Long userId, Long itemId, Item item);
}
