package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class InMemoryItemRepositoryImpl implements ItemRepository{
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
            if(userItems == null) {
                userItems = new ArrayList<>();
            }
            userItems.add(item);
            return userItems;
        });
        return item;
    }

    @Override
    public Item update(long userId, long itemId, Item item) {
        Item itemRemove = items.get(userId).stream()
                .filter(f -> f.getId() == itemId)
                .findFirst()
                .orElseThrow();
        items.get(userId).remove(itemRemove);
        items.get(userId).add(item);
        return item;
    }

    @Override
    public List<Item> getItemsBuUser(long userId) {
        return items.getOrDefault(userId, Collections.emptyList());
    }

    @Override
    public List<Item> searchItem(String text) {
        return items.values().stream()
                .flatMap(Collection::stream)
                .filter(item -> item.getName().contains(text) || item.getDescription().contains(text))
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
