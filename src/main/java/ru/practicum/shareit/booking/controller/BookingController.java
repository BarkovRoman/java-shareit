package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto add(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @Validated @RequestBody BookingDto bookingDto) {
        return bookingService.add(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                             @RequestParam Boolean approved,
                             @PathVariable Long bookingId) {
        return bookingService.update(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long bookingId) {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping()
    public List<BookingResponseDto> getByBookerIdAndState(@RequestHeader("X-Sharer-User-Id") Long userId,
                                               @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getByBookerIdAndState(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllOwnerId(userId, state);
    }
}
