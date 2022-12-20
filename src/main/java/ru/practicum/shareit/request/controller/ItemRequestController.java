package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestCreateDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestCreateDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.add(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestResponseDto> getByUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.getByUser(userId);
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long requestId) {
        return itemRequestService.getById(requestId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponseDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "0") int size) {
        return itemRequestService.getAll(from, size);
    }
}
