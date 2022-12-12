package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "id", source = "itemId")
    @Mapping(target = "owner", source = "userId")
    Item toItem(ItemDto itemDto, Long userId, Long itemId);

    ItemDto toItemDto(Item item);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "lastBooking", source = "lastBooking")
    @Mapping(target = "nextBooking", source = "nextBooking")
    @Mapping(target = "comments", source = "comments")
    ItemBookingDto toItemBookingDto(Item item, LastNextItemShortDto lastBooking, LastNextItemShortDto nextBooking, Long id, List<CommentShortResponseDto> comments);

    @Mapping(target = "author", source = "user")
    @Mapping(target = "item", source = "item")
    @Mapping(target = "id", ignore = true)
    Comment toComment(CommentDto commentDto, Item item, User user);

    @Mapping(target = "authorName", source = "author")
    CommentResponseDto toCommentResponseDto(Comment comment, String author);

}
