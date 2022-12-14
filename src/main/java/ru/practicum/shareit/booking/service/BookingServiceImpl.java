package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
            return bookingRepository.findByBooker_IdAndStatusAndEndBefore(userId,
                            BookingStatus.APPROVED,
                            LocalDateTime.now(),
                            Sort.by(Sort.Direction.DESC, "end")).stream()
                    .map(mapper::bookingToBookingResponseDto)
                    .collect(Collectors.toList());
        }

        if (!status.equals(BookingStatus.ALL)) {
            return bookingRepository.findByBookerIdAndStatus(userId, status, Sort.by(Sort.Direction.DESC, "end")).stream()
                    .map(mapper::bookingToBookingResponseDto)
                    .collect(Collectors.toList());
        }
        return bookingRepository.findByBookerId(userId, Sort.by(Sort.Direction.DESC, "end")).stream()
                .map(mapper::bookingToBookingResponseDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<BookingResponseDto> getAllOwnerId(Long userId, String state) {
        isExistsUserById(userId);
        BookingStatus status = isExistsStatus(state);
        if (status.equals(BookingStatus.PAST)) {
            return bookingRepository.findByOwnerIdAndStatusIsBefore(userId, BookingStatus.APPROVED, LocalDateTime.now()).stream()
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
        switch (status) {
            case ("REJECTED") :
            case ("CURRENT") :
                return BookingStatus.REJECTED;
            case ("APPROVED") :
                return BookingStatus.APPROVED;
            case ("PAST") :
                return BookingStatus.PAST;
            case ("WAITING") :
                return BookingStatus.WAITING;
            case ("ALL") :
            case ("FUTURE") :
                return BookingStatus.ALL;
            default: throw new IllegalRequestException("Unknown state: UNSUPPORTED_STATUS");
        }
    }
}

