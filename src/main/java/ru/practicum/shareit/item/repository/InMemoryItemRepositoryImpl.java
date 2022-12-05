/*
package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemRepositoryImpl implements ItemRepository {
    private final Map<Long, List<Item>> itemsByUserId = new HashMap<>();
    private final Map<Long, Item> items = new HashMap<>();
    private long id;

    @Override
    public Optional<Item> getById(long itemId) {          // Можно было бы использовать return Optional.ofNullable(users.get(id));
        return Optional.ofNullable(items.get(itemId));
    }

    @Override
    public Item add(Item item, long userId) {
        item.setId(getId());
        items.put(item.getId(), item);
        itemsByUserId.compute(userId, (id, userItems) -> {
            if (userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });
        return item;
    }

    @Override
    public Item update(long userId, long itemId, Item item) {
        Item itemUpdate = items.get(itemId);
        if (itemUpdate.getOwner() != userId) {
            throw new NotFoundException(String.format("Item id=%s не пренадлежит User id=%s", itemId, userId));
        }
        if (item.getName() != null && !item.getName().isBlank()) {
            itemUpdate.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            itemUpdate.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemUpdate.setAvailable(item.getAvailable());
        }
        return itemUpdate;
    }

    @Override
    public List<Item> getByUser(long userId) {
        return itemsByUserId.getOrDefault(userId, Collections.emptyList());
    }

    @Override
    public List<Item> search(String text) {
        return itemsByUserId.values().stream()
                .flatMap(Collection::stream)
                .filter(item ->
                        (item.getName().toLowerCase().trim().contains(text.toLowerCase()) ||
                                item.getDescription().toLowerCase().trim().contains(text.toLowerCase())) &&
                                item.getAvailable())
                .collect(Collectors.toList());
    }

    private long getId() {
        return ++id;
    }
}
*/
