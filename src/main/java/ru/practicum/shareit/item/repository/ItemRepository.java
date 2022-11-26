package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Optional<Item> getById(long itemId);

    Item add(Item item, long userId);

    Item update(long userId, long itemId, Item item);

    List<Item> getByUser(long userId);

    List<Item> search(String text);
}
