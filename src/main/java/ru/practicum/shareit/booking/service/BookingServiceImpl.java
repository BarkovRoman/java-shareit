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
        BookingResponseDto bookingResponse = mapper.bookingToBookingResponseDto(
                booking,
                mapper.itemResponseDto(item),
                mapper.bookerResponseDto(user),
                booking.getId());
        log.debug("Добавлен Booking {}", booking);
        return bookingResponse;
    }

    @Override
    public BookingResponseDto update(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Item id=%s не найден", bookingId)));
        User user = isExistsUserById(booking.getBooker().getId());
        if (approved) booking.setStatus(BookingStatus.APPROVED);
        if (!approved) booking.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking);
        Item item = isExistsAvailableItem(booking.getItem().getId());
        log.debug("Обновление Booking {}", booking);
        return mapper.bookingToBookingResponseDto(
                booking,
                mapper.itemResponseDto(item),
                mapper.bookerResponseDto(user),
                booking.getId());
    }

    @Override
    public BookingResponseDto getById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Item id=%s не найден", bookingId)));
        User user = isExistsUserById(booking.getBooker().getId());
        Item item = isExistsAvailableItem(booking.getItem().getId());
        return mapper.bookingToBookingResponseDto(
                booking,
                mapper.itemResponseDto(item),
                mapper.bookerResponseDto(user),
                booking.getId());
    }

    @Override
    public List<BookingDto> getByBookerIdAndState(Long userId, BookingStatus state) {
        isExistsUserById(userId);
        /*if (state.toString().equals("ALL")) {
            return bookingRepository.findByBookerId(userId).stream()
                    .map(mapper::toBookingDto)
                    .collect(Collectors.toList());
        }*/
        /*return bookingRepository.findByBookerIdAndStatus(userId, state).stream()
                .map(mapper::toBookingDto)
                .collect(Collectors.toList());*/
        return null;
    }

    @Override
    public List<BookingDto> getAllOwnerId(Long userId, BookingStatus state) {
        isExistsUserById(userId);
       /* if (state.toString().equals("ALL")) {
            return bookingRepository.findByBookerId(userId).stream()
                    .map(mapper::toBookingDto)
                    .collect(Collectors.toList());
        }*/
        /*return bookingRepository.findByBookerIdAndStatus(userId, state).stream()
                .map(mapper::toBookingDto)
                .collect(Collectors.toList());*/
        return null;
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
