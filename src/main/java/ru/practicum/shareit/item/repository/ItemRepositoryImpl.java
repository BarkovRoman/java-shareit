package ru.practicum.shareit.item.repository;

import org.springframework.context.annotation.Lazy;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;

public class ItemRepositoryImpl implements ItemRepositoryCustom {

    private final ItemRepository itemRepository;
    public ItemRepositoryImpl(@Lazy ItemRepository itemRepository)  {
        this.itemRepository = itemRepository;
    }
    public Item update(Long userId, Long itemId, Item item) {
        Item itemUpdate = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item id=%s не найден", itemId)));
        if (!itemUpdate.getOwner().equals(userId)) {
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
        itemRepository.save(itemUpdate);
        return itemUpdate;
    }
}
