package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemDto add(ItemDto itemDto, long userId) {
        isExistsUserById(userId);
        Item item = ItemMapper.toItem(itemDto, userId, 0);
        Item itemNew = itemRepository.add(item, userId);
        itemDto = ItemMapper.toItemDto(itemNew);
        log.debug("Добавлен item {}", itemNew);
        return itemDto;
    }

    @Override
    public ItemDto update(long userId, long itemId, ItemDto itemDto) {
        isExistsUserById(userId);
        isExistsItemById(itemId);
        Item item = ItemMapper.toItem(itemDto, userId, itemId);
        itemDto = ItemMapper.toItemDto(itemRepository.update(userId, itemId, item));
        log.debug("Обновление item {}", item);
        return itemDto;
    }

    @Override
    public List<ItemDto> getByUser(long userId) {
        isExistsUserById(userId);
        return itemRepository.getByUser(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getById(long itemId) {
        isExistsItemById(itemId);
        return ItemMapper.toItemDto(itemRepository.getById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item id=%s не найден", itemId))));
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemRepository.search(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private void isExistsUserById(long id) {
        userRepository.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s не найден", id)));
    }

    private void isExistsItemById(long id) {
        itemRepository.getById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s не найден", id)));
    }
}
