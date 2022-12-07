package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repositry.BookingRepository;

import java.util.List;

@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{
    private final BookingRepository bookingRepository;
    @Override
    public BookingDto add(BookingDto bookingDto, Long userId) {
        return null;
    }

    @Override
    public BookingDto update(Long userId, Long bookingId, Boolean approved) {
        return null;
    }

    @Override
    public BookingDto getById(Long userId, Long bookingId) {
        return null;
    }

    @Override
    public List<BookingDto> getAllState(Long userId, BookingStatus state) {
        return null;
    }

    @Override
    public List<BookingDto> getAllOwner(Long userId, BookingStatus state) {
        return null;
    }
}
