package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom {

    @Query(" select i from Item i " +
            "where upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%')) " +
            "and i.available = true ")
    List<Item> search(String text);

    List<Item> findByOwner(Long userId);

    /*Optional<Item> getById(long itemId);

    Item add(Item item, long userId);

    Item update(long userId, long itemId, Item item);

    List<Item> getByUser(long userId);

    List<Item> search(String text);*/
}
