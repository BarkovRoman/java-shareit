package ru.practicum.shareit.item.service;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto add(ItemDto itemDto, Long userId);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    List<ItemDto> getByUser(Long userId);

    ItemDto getById(Long itemId);

    List<ItemDto> search(String text);
}
