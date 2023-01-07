package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @Valid @RequestBody ItemRequestDto itemDto) {
        log.info("Creating item {}, userId={}", itemDto, userId);
        return itemClient.add(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestHeader("X-Sharer-User-Id") Long userId,
                          @RequestBody ItemRequestDto itemDto,
                          @PathVariable Long itemId) {
        log.info("Update item={}, userId={}, itemId={}", itemDto, userId, itemId);
        return itemClient.update(userId, itemId, itemDto);
    }

    @GetMapping
    public ResponseEntity<Object> getByUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @Positive @RequestParam(name = "size", defaultValue = "2") Integer size) {
        log.info("Get item by userId={}, from={}, size={}", userId, from, size);
        return itemClient.getByUser(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long itemId) {
        log.info("Get itemId={}, userId={}", itemId, userId);
        return itemClient.getById(itemId, userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam String text,
                                             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Search text={}, from={}, size={}", text, from, size);
        return itemClient.search(text, from, size, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComments(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @RequestBody @Valid CommentRequestDto commentDto,
                                          @PathVariable Long itemId) {
        log.info("Create comment={}, userId={}, itemId={}", commentDto, userId, itemId);
        return itemClient.addComments(userId, itemId, commentDto);
    }
}
