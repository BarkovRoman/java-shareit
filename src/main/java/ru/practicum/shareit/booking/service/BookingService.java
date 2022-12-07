package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

public interface BookingService {
    BookingDto add(BookingDto bookingDto, Long userId);

    BookingDto update(Long userId, Long bookingId, Boolean approved);

    BookingDto getById(Long userId, Long bookingId);

    List<BookingDto> getAllState(Long userId, BookingStatus state);

    List<BookingDto> getAllOwner(Long userId, BookingStatus state);
}
