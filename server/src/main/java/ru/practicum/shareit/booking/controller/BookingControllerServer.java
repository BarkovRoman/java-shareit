package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController

@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingControllerServer {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponseDto add(@RequestHeader("X-Sharer-User-Id") Long userId,    // bookItem
                                  @RequestBody BookingDto bookingDto) {
        return bookingService.add(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto update(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @RequestParam Boolean approved,
                                     @PathVariable Long bookingId) {
        return bookingService.update(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,    // getBooking
                                      @PathVariable Long bookingId) {
        return bookingService.getById(userId, bookingId);
    }

    @GetMapping()
    public List<BookingResponseDto> getByBookerIdAndState(@RequestHeader("X-Sharer-User-Id") Long userId,     // getBookings
                                                          @RequestParam Status state,
                                                          @RequestParam Integer from,
                                                          @RequestParam Integer size) {
        return bookingService.getByBookerIdAndState(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingResponseDto> getAllOwnerId(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam Status state,
                                                  @RequestParam Integer from,
                                                  @RequestParam Integer size) {
        return bookingService.getAllOwnerId(userId, state, from, size);
    }
}
