package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {

    Optional<Item> getBuId(long itemId);

    Item add(Item item, long userId);

    Item update(long userId, long itemId, Item item);

    List<Item> getItemsBuUser(long userId);

    List<Item> searchItem(String text);
}
