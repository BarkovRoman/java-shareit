package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentResponseDto;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto add(ItemDto itemDto, Long userId);

    ItemDto update(Long userId, Long itemId, ItemDto itemDto);

    List<ItemBookingDto> getByUser(Long userId);

    ItemBookingDto getById(Long itemId, Long userId);

    List<ItemDto> search(String text);

    CommentResponseDto addComments(Long userId, Long itemId, CommentDto commentDto);
}
