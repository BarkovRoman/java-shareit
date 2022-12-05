package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> getById(long itemId);

    Item add(Item item, long userId);

    Item update(long userId, long itemId, Item item);

    List<Item> getByUser(long userId);

    List<Item> search(String text);
}
