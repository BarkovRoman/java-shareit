package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repositry.BookingRepository;
import ru.practicum.shareit.exception.ExistingValidationException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.*;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper mapper;

    @Override
    public ItemDto add(ItemDto itemDto, Long userId) {
        isExistsUserById(userId);
        Item item = itemRepository.save(mapper.toItem(itemDto, userId, null));
        itemDto = mapper.toItemDto(item);
        log.debug("Добавлен item {}", item);
        return itemDto;
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        isExistsUserById(userId);
        isExistsItemById(itemId);
        Item item = mapper.toItem(itemDto, userId, itemId);
        itemDto = mapper.toItemDto(itemRepository.update(userId, itemId, item));
        log.debug("Обновление item {}", item);
        return itemDto;
    }

    @Override
    public List<ItemBookingDto> getByUser(Long userId) {
        isExistsUserById(userId);
        List<Item> items = itemRepository.findByOwner(userId);
        return items.stream()
                .map(f -> getById(f.getId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    public ItemBookingDto getById(Long itemId, Long userId) {
        LinkedList<LastNextItemShortDto> bookings = bookingRepository.getBookingByItem(itemId, userId);
        List<CommentShortResponseDto> comments = commentRepository.findCommentByItem_Id(itemId);
        if (bookings.size() == 0) {
            return mapper.toItemBookingDto(itemRepository.findById(itemId)
                            .orElseThrow(() -> new NotFoundException(String.format("Item id=%s не найден", itemId))),
                    null, null, itemId, comments);
        }
        return mapper.toItemBookingDto(itemRepository.findById(itemId)
                        .orElseThrow(() -> new NotFoundException(String.format("Item id=%s не найден", itemId))),
                bookings.getFirst(), bookings.getLast(), itemId, comments);
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemRepository.search(text).stream()
                .map(mapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDto addComments(Long userId, Long itemId, CommentDto commentDto) {
        isExistsItemById(itemId);
        isExistsUserById(userId);

        if (!bookingRepository.existsBookingByItem_IdAndBooker_IdAndStatusAndEndBefore(itemId, userId, BookingStatus.APPROVED, LocalDateTime.now())) {
            throw new ExistingValidationException("Comment не может быть создан");
        }
        Booking booking = bookingRepository.findBookingByItem_IdAndBooker_IdAndStatusAndEndBefore(itemId, userId, BookingStatus.APPROVED, LocalDateTime.now());
        Comment comment = commentRepository.save(mapper.toComment(commentDto, booking.getItem(), booking.getBooker()));
        log.debug("Добавлен Comment {}", comment);
        return mapper.toCommentResponseDto(comment, comment.getAuthor().getName());
    }

    private void isExistsUserById(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User id=%s не найден", id)));
    }

    private void isExistsItemById(Long id) {
        itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Item id=%s не найден", id)));
    }
}

