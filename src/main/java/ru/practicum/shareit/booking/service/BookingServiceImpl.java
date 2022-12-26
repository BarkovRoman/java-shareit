package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repositry.BookingRepository;
import ru.practicum.shareit.exception.ExistingValidationException;
import ru.practicum.shareit.exception.IllegalRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.shareit.booking.model.BookingStatus.PAST;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper mapper;

    @Transactional
    @Override
    public BookingResponseDto add(BookingDto bookingDto, Long userId) {
        User user = isExistsUserById(userId);
        Item item = isExistsAvailableItem(bookingDto.getItemId(), userId);
        Booking booking = bookingRepository.save(mapper.toBooking(bookingDto, item, user));
        log.debug("Добавлен Booking {}", booking);
        return mapper.bookingToBookingResponseDto(booking);
    }

    @Transactional
    @Override
    public BookingResponseDto update(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Booking id=%s не найден", bookingId)));
        if (!booking.getItem().getOwner().equals(userId)) {
            throw new NotFoundException(String.format("Item id=%s не принадлежит User id=%s", bookingId, userId));
        }
        if (booking.getStatus() == BookingStatus.APPROVED && approved.equals(true)) {
            throw new IllegalRequestException(String.format("Booking id=%s уже имеет статус подтверждения", bookingId));
        }
        if (approved) booking.setStatus(BookingStatus.APPROVED);
        else booking.setStatus(BookingStatus.REJECTED);
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
                "Booking id=%s не принадлежит User id=%s", bookingId, userId));
    }

    @Override
    public List<BookingResponseDto> getByBookerIdAndState(Long userId, Status state, Integer from, Integer size) {
        isExistsUserById(userId);
        Sort sort = Sort.by(Sort.Direction.DESC, "end");
        final PageRequest page = PageRequest.of(from, size, sort);
        Page<Booking> bookings;
        switch (isExistsStatus(state)) {
            case PAST:
                bookings = bookingRepository.findByBooker_IdAndStatusAndEndBefore(userId,
                        BookingStatus.APPROVED,
                        LocalDateTime.now(),
                        page);
                break;
            case ALL:
                final PageRequest page1 = PageRequest.of((from / size), size, sort);
                bookings = bookingRepository.findByBookerId(userId, page1);
                break;
            default:
                bookings = bookingRepository.findByBookerIdAndStatus(userId, isExistsStatus(state), page);
                break;
        }
        return bookings.getContent().stream()
                .map(mapper::bookingToBookingResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getAllOwnerId(Long userId, Status state, Integer from, Integer size) {
        isExistsUserById(userId);
        final PageRequest page = PageRequest.of(from, size);
        Page<Booking> bookings;
        switch (isExistsStatus(state)) {
            case PAST:
                bookings = bookingRepository.findByOwnerIdAndStatusIsBefore(userId, BookingStatus.APPROVED, LocalDateTime.now(), page);
                break;
            case ALL:
                bookings = bookingRepository.findByOwnerId(userId, page);
                break;
            default:
                bookings = bookingRepository.findByOwnerIdAndStatus(userId, isExistsStatus(state), page);
                break;
        }
        return bookings.stream()
                .map(mapper::bookingToBookingResponseDto)
                .collect(Collectors.toList());
    }

    private User isExistsUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s не найден", id)));
    }

    private Item isExistsAvailableItem(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item id=%s не найден", itemId)));
        if (!item.getAvailable()) {
            throw new ExistingValidationException(String.format("Item id=%s заблокирован для бронирования", itemId));
        }
        if (item.getOwner().equals(userId)) {
            throw new NotFoundException(String.format("User id=%s не может забронировать Item id=%s", userId, itemId));
        }
        return item;
    }

    private BookingStatus isExistsStatus(Status status) {
        switch (status) {
            case REJECTED:
            case CURRENT:
                return BookingStatus.REJECTED;
            case APPROVED:
                return BookingStatus.APPROVED;
            case PAST:
                return PAST;
            case WAITING:
                return BookingStatus.WAITING;
            case ALL:
            case FUTURE:
                return BookingStatus.ALL;
            default:
                throw new IllegalRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}

