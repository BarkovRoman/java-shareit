package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequestCreateDto add(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequestResponseDto> getByUser(Long userId);

    ItemRequestResponseDto getById(Long requestId, Long userId);

    List<ItemRequestResponseDto> getAll(Integer from, Integer size, Long userId);
}
