package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

public interface BookingService {
    BookingResponseDto add(BookingDto bookingDto, Long userId);

    BookingResponseDto update(Long userId, Long bookingId, Boolean approved);

    BookingResponseDto getById(Long userId, Long bookingId);

    List<BookingResponseDto> getByBookerIdAndState(Long userId, Status state, Integer from, Integer size);

    List<BookingResponseDto> getAllOwnerId(Long userId, Status state, Integer from, Integer size);
}
