package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repositry.BookingRepository;
import ru.practicum.shareit.exception.ExistingValidationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
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
    public BookingDto add(BookingDto bookingDto, Long userId) {
        isExistsUserById(userId);
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ExistingValidationException(String.format("start =%s позже end =%s", bookingDto.getStart(), bookingDto.getEnd()));
        }
        Item item = isExistsAvailableItem(bookingDto.getItemId());
        Booking booking = bookingRepository.save(mapper.toBooking(bookingDto, userId));
        //bookingDto = mapper.toBookingDto(booking, item /*item.getId(), item.getName()*/);
        log.debug("Добавлен Booking {}", booking);
        return bookingDto;
    }

    @Override
    public BookingDto update(Long userId, Long bookingId, Boolean approved) {
        isExistsUserById(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Item id=%s не найден", bookingId)));
        if (approved) booking.setStatus(BookingStatus.APPROVED);
        if (!approved) booking.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking);
        Item item = isExistsAvailableItem(booking.getItemId());
        log.debug("Обновление Booking {}", booking);
        return mapper.toBookingDto(booking, userId);
    }

    @Override
    public BookingDto getById(Long userId, Long bookingId) {
        isExistsUserById(userId);
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Item id=%s не найден", bookingId)));
        Item item = isExistsAvailableItem(booking.getItemId());
        return mapper.toBookingDto(booking, userId);
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

    private void isExistsUserById(Long id) {
        userRepository.findById(id)
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
