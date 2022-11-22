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
    public ItemDto addItem(ItemDto itemDto, long userId) {
        checkingUserBuId(userId);
        Item item = ItemMapper.toItem(itemDto, 0);
        itemRepository.add(item, userId);
        itemDto = ItemMapper.toItemDto(itemRepository.getBuId(item.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Item id=%s не coздан", item.getId())))
        );
        log.debug("Добавлен item {}", item);
        return itemDto;
    }

    @Override
    public ItemDto update(long userId, long itemId, ItemDto itemDto) {
        checkingUserBuId(userId);
        checkingItemBuId(itemId);


        Item item = ItemMapper.toItem(itemDto, itemId);
        itemRepository.update(userId, itemId, item);
        itemDto = ItemMapper.toItemDto(
                itemRepository.getBuId(item.getId())
                        .orElseThrow(() -> new NotFoundException(String.format("Item id=%s не обновлен", item.getId()))));
        log.debug("Обновление item {}", item);
        return itemDto;
    }

    @Override
    public List<ItemDto> getItems(long userId) {
        checkingUserBuId(userId);
        return itemRepository.getItemsBuUser(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getBuId(long itemId) {
        checkingItemBuId(itemId);
        return ItemMapper.toItemDto(itemRepository.getBuId(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item id=%s не найден", itemId))));
    }

    @Override
    public List<ItemDto> searchItem(String text) {
        return itemRepository.searchItem(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private void checkingUserBuId(long id) {
        userRepository.getBuId(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s не найден", id)));
    }

    private void checkingItemBuId(long id) {
        itemRepository.getBuId(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s не найден", id)));
    }
}
