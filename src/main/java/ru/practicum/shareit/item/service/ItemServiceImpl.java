package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.*;

import static java.util.stream.Collectors.*;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemMapper mapper;

    @Override
    @Transactional
    public ItemDto add(ItemDto itemDto, Long userId) {
        isExistsUserById(userId);
        Item item = itemRepository.save(mapper.toItem(itemDto, userId, null));
        itemDto = mapper.toItemDto(item);
        log.debug("Добавлен item {}", item);
        return itemDto;
    }

    @Override
    @Transactional
    public ItemDto update(Long userId, Long itemId, ItemDto itemDto) {
        isExistsUserById(userId);
        isExistsItemById(itemId);
        Item item = mapper.toItem(itemDto, userId, itemId);
        Item itemUpdate = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item id=%s не найден", itemId)));
        if (!itemUpdate.getOwner().equals(userId)) {
            throw new NotFoundException(String.format("Item id=%s не пренадлежит User id=%s", itemId, userId));
        }
        if (item.getName() != null && !item.getName().isBlank()) {
            itemUpdate.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            itemUpdate.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemUpdate.setAvailable(item.getAvailable());
        }
        itemDto = mapper.toItemDto(itemUpdate);
        log.debug("Обновление item {}", item);
        return itemDto;
    }

    @Override
    public List<ItemBookingDto> getByUser(Long userId) {
        isExistsUserById(userId);
        List<Item> items = itemRepository.findByOwner(userId);
        Map<Item, List<Comment>> comments = commentRepository.findByItemIn(items, Sort.by(DESC, "created"))
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));

        Map<Item, List<Booking>> bookings = bookingRepository.findByItemInAndStatus(items, BookingStatus.APPROVED, Sort.by(ASC, "end"))
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));
        List<CommentResponseDto> commentsShort;
        List<ItemBookingDto> itemBookingDto = new ArrayList<>();

        for (Item item : items) {
            int bookigsSize = 0;
            if (bookings.containsKey(item)) {
                bookigsSize = bookings.get(item).size();
            }
            commentsShort = mapper.mapComment(comments.get(item));

            itemBookingDto.add(mapper.toItemBookingCommentDto(item,
                    bookigsSize == 0 ? null : mapper.toLastNextItemDto(bookings.get(item)
                            .stream()
                            .filter(date -> date.getStart().isBefore(LocalDateTime.now()) || date.getEnd().isBefore(LocalDateTime.now()))
                            .min((o1, o2) -> o2.getEnd().compareTo(o1.getEnd()))
                            .orElse(null)),
                    bookigsSize == 0 ? null : mapper.toLastNextItemDto(bookings.get(item).stream()
                            .filter(date -> date.getStart().isAfter(LocalDateTime.now()))
                            .min((o1, o2) -> o2.getStart().compareTo(o1.getStart()))
                            .orElse(null)),
                    item.getId(),
                    commentsShort));
        }
        return itemBookingDto;
    }

    @Override
    public ItemBookingDto getById(Long itemId, Long userId) {
        List<Booking> bookings = bookingRepository.getBookingByItem(itemId, userId, BookingStatus.APPROVED);
        List<CommentResponseDto> comments = mapper.mapComment(commentRepository.findCommentByItem_Id(itemId));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Item id=%s не найден", itemId)));
        ItemBookingDto itemBookingDto;
        itemBookingDto = mapper.toItemBookingCommentDto(item,
                bookings.size() == 0 ? null : mapper.toLastNextItemDto(bookings
                        .stream()
                        .filter(date -> date.getStart().isBefore(LocalDateTime.now()) || date.getEnd().isBefore(LocalDateTime.now()))
                        .min((o1, o2) -> o2.getEnd().compareTo(o1.getEnd()))
                        .orElse(null)),
                bookings.size() == 0 ? null : mapper.toLastNextItemDto(bookings
                        .stream()
                        .filter(date -> date.getStart().isAfter(LocalDateTime.now()))
                        .min((o1, o2) -> o2.getStart().compareTo(o1.getStart()))
                        .orElse(null)),
                item.getId(),
                comments);
        return itemBookingDto;
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemRepository.search(text).stream()
                .map(mapper::toItemDto)
                .collect(toList());
    }

    @Override
    @Transactional
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

