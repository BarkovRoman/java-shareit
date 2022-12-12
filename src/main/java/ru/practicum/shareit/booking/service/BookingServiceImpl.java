package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
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
        Item item = isExistsAvailableItem(bookingDto.getItemId(), userId);
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
        if (booking.getStatus() == BookingStatus.APPROVED) {
            throw new IllegalRequestException(String.format("Booking id=%s уже имеет статус подтверждения", bookingId));
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
    public List<BookingResponseDto> getByBookerIdAndState(Long userId, String state) {
        isExistsUserById(userId);
        BookingStatus status = isExistsStatus(state);

        if (status.equals(BookingStatus.PAST)) {
            return bookingRepository.findByBooker_IdAndStatusAndEndBeforeOrderByEndDesc(userId, BookingStatus.APPROVED, LocalDateTime.now()).stream()
                    .map(mapper::bookingToBookingResponseDto)
                    .collect(Collectors.toList());
        }

        if (!status.equals(BookingStatus.ALL)) {
            return bookingRepository.findByBookerIdAndStatusOrderByEndDesc(userId, status).stream()
                    .map(mapper::bookingToBookingResponseDto)
                    .collect(Collectors.toList());
        }
        return bookingRepository.findByBookerIdOrderByEndDesc(userId).stream()
                .map(mapper::bookingToBookingResponseDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<BookingResponseDto> getAllOwnerId(Long userId, String state) {
        isExistsUserById(userId);
        BookingStatus status = isExistsStatus(state);
        if (status.equals(BookingStatus.PAST)) {
            return bookingRepository.findByOwnerIdAndStatus(userId, BookingStatus.APPROVED).stream()
                    .filter(f -> f.getEnd().isBefore(LocalDateTime.now()))
                    .map(mapper::bookingToBookingResponseDto)
                    .collect(Collectors.toList());
        }
        if (!status.equals(BookingStatus.ALL)) {
            return bookingRepository.findByOwnerIdAndStatus(userId, status).stream()
                    .map(mapper::bookingToBookingResponseDto)
                    .collect(Collectors.toList());
        }
        return bookingRepository.findByOwnerId(userId).stream()
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

    private BookingStatus isExistsStatus(String status) {
        if (status.equals(BookingStatus.CURRENT.toString())) return BookingStatus.REJECTED;
        if (status.equals(BookingStatus.REJECTED.toString())) return BookingStatus.REJECTED;

        if (status.equals(BookingStatus.APPROVED.toString())) return BookingStatus.APPROVED;
        if (status.equals(BookingStatus.PAST.toString())) return BookingStatus.PAST;

        if (status.equals(BookingStatus.WAITING.toString())) return BookingStatus.WAITING;

        if (status.equals(BookingStatus.ALL.toString())) return BookingStatus.ALL;
        if (status.equals(BookingStatus.FUTURE.toString())) return BookingStatus.ALL;

        throw new IllegalRequestException("Unknown state: UNSUPPORTED_STATUS");
    }
}

