package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repositry.BookingRepository;
import ru.practicum.shareit.exception.ExistingValidationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper mapper;

    @Override
    public BookingResponseDto add(BookingDto bookingDto, Long userId) {
        User user = isExistsUserById(userId);
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ExistingValidationException(String.format("start =%s позже end =%s", bookingDto.getStart(), bookingDto.getEnd()));
        }
        Item item = isExistsAvailableItem(bookingDto.getItemId());
        Booking booking = bookingRepository.save(mapper.toBooking(bookingDto, item, user));
        log.debug("Добавлен Booking {}", booking);
        return mapper.bookingToBookingResponseDto(booking);
    }

    @Override
    public BookingResponseDto update(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Booking id=%s не найден", bookingId)));
        if (!booking.getItem().getOwner().equals(userId)) {
            throw new NotFoundException(String.format("Item id=%s не пренадлежит User id=%s", bookingId, userId));
        }
        if (approved) booking.setStatus(BookingStatus.APPROVED);
        if (!approved) booking.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking);
        log.debug("Обновление Booking {}", booking);
        return mapper.bookingToBookingResponseDto(booking);
    }

    @Override
    public BookingResponseDto getById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Booking id=%s не найден", bookingId)));
        if (booking.getBooker().getId().equals(userId) || booking.getItem().getOwner().equals(userId)) {
            return mapper.bookingToBookingResponseDto(booking);
        }
        throw new NotFoundException(String.format(
                "Booking id=%s не пренадлежит User id=%s", bookingId, userId));
    }

    @Override
    public List<BookingResponseDto> getByBookerIdAndState(Long userId, BookingStatus state) {
        isExistsUserById(userId);
        return bookingRepository.findByBookerIdOrderByEndDesc(userId).stream()
                .map(mapper::bookingToBookingResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getAllOwnerId(Long userId, BookingStatus state) {
        isExistsUserById(userId);

        List<Booking> bookings = bookingRepository.findByOwnerId(userId);

        return bookings.stream()
                .map(mapper::bookingToBookingResponseDto)
                .collect(Collectors.toList());
    }

    private User isExistsUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s не найден", id)));
    }

    private Item isExistsAvailableItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item id=%s не найден", itemId)));
        if (!item.getAvailable()) {
            throw new ExistingValidationException(String.format("Item id=%s заблокирован для бронирования", itemId));
        }
        return item;
    }
}
