package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemRepositoryImpl implements ItemRepository {
    private final Map<Long, List<Item>> items = new HashMap<>();

    @Override
    public Optional<Item> getBuId(long itemId) {
        return items.values().stream()
                .flatMap(Collection::stream)
                .filter(item -> item.getId() == itemId)
                .findFirst();
    }

    @Override
    public Item add(Item item, long userId) {
        item.setId(getId());
        items.compute(userId, (id, userItems) -> {
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
        Item itemUpdate;
        try {
            itemUpdate = items.get(userId).stream()
                    .filter(f -> f.getId() == itemId)
                    .findFirst()
                    .orElseThrow();
        } catch (Throwable e) {
            throw new NotFoundException(String.format("Item id=%s не пренадлежит User id=%s", itemId, userId));
        }
        items.get(userId).remove(itemUpdate);
        if (item.getName() != null) {
            itemUpdate.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemUpdate.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemUpdate.setAvailable(item.getAvailable());
        }
        items.get(userId).add(itemUpdate);
        return itemUpdate;
    }

    @Override
    public List<Item> getItemsBuUser(long userId) {
        return items.getOrDefault(userId, Collections.emptyList());
    }

    @Override
    public List<Item> searchItem(String text) {
        return items.values().stream()
                .flatMap(Collection::stream)
                .filter(item ->
                        item.getName().toLowerCase().trim().contains(text.toLowerCase()) ||
                                item.getDescription().toLowerCase().trim().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }

    private long getId() {
        return items.values()
                .stream()
                .flatMap(Collection::stream)
                .mapToLong(Item::getId)
                .max()
                .orElse(0) + 1;
    }
}
