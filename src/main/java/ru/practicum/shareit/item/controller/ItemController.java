package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                       @Valid @RequestBody ItemDto itemDto) {
        return itemService.add(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @RequestBody ItemDto itemDto,
                          @PathVariable long itemId) {
        return itemService.update(userId, itemId, itemDto);
    }

    @GetMapping
    public List<ItemBookingDto> getByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getByUser(userId);
    }

    @GetMapping("/{itemId}")
    public ItemBookingDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long itemId) {
        return itemService.getById(itemId, userId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestParam String text) {
        return text.isBlank() ? Collections.emptyList() : itemService.search(text);
    }
}
