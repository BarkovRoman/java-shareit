package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repositry.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
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
    public BookingDto add(BookingDto bookingDto, Long userId) {
        isExistsUserById(userId);
        Item item = isBelongItemOfUser(userId, bookingDto.getItemId());
        Booking booking = bookingRepository.save(mapper.toBooking(bookingDto, userId));
        bookingDto = mapper.toBookingDto(booking, item.getId(), item.getName());
        log.debug("Добавлен Booking {}", booking);
        return bookingDto;
    }

    @Override
    public BookingDto update(Long userId, Long bookingId, Boolean approved) {
        isExistsUserById(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Item id=%s не найден", bookingId)));
        if (booking.getBookerId().equals(userId)) {
            throw new NotFoundException(String.format("Booking id=%s не пренадлежит User id=%s", bookingId, userId));
        }
        if (approved) booking.setStatus(BookingStatus.APPROVED);
        if (!approved) booking.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking);
        log.debug("Обновление Booking {}", booking);
        return mapper.toBookingDto(booking);
    }

    @Override
    public BookingDto getById(Long userId, Long bookingId) {
        isExistsUserById(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Item id=%s не найден", bookingId)));
        if (booking.getBookerId().equals(userId) || booking.getItemId().equals(userId)) {
            throw new NotFoundException(String.format("Booking id=%s не пренадлежит User id=%s", bookingId, userId));
        }
        return mapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getByBookerIdAndState(Long userId, BookingStatus state) {
        isExistsUserById(userId);
        if (state.toString().equals("ALL")) {
            return bookingRepository.findByBookerId(userId).stream()
                    .map(mapper::toBookingDto)
                    .collect(Collectors.toList());
        }
        return bookingRepository.findByBookerIdAndStatus(userId, state).stream()
                .map(mapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getAllOwnerId(Long userId, BookingStatus state) {
        isExistsUserById(userId);
        if (state.toString().equals("ALL")) {
            return bookingRepository.findByBookerId(userId).stream()
                    .map(mapper::toBookingDto)
                    .collect(Collectors.toList());
        }
        return bookingRepository.findByBookerIdAndStatus(userId, state).stream()
                .map(mapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private void isExistsUserById(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s не найден", id)));
    }

    private Item isBelongItemOfUser(Long userId, Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item id=%s не найден", itemId)));
        if (!item.getOwner().equals(userId)) {
            throw new NotFoundException(String.format("Item id=%s не пренадлежит User id=%s", itemId, userId));
        }
        return item;
    }
}
