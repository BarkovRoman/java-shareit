package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "id", source = "itemDto.id")
    @Mapping(target = "owner", source = "userId")
    @Mapping(target = "request", source = "request")
    @Mapping(target = "description", source = "itemDto.description")
    Item toItem(ItemDto itemDto, Long userId, ItemRequest request);

    @Mapping(target = "requestId", source = "item.request.id")
    ItemDto toItemDto(Item item);

    @Mapping(target = "author", source = "user")
    @Mapping(target = "item", source = "item")
    @Mapping(target = "id", ignore = true)
    Comment toComment(CommentDto commentDto, Item item, User user);

    @Mapping(target = "authorName", source = "author")
    CommentResponseDto toCommentResponseDto(Comment comment, String author);

    List<CommentResponseDto> mapComment(List<Comment> commentLit);

    default CommentResponseDto mapComment(Comment comment) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setId(comment.getId());
        commentResponseDto.setText(comment.getText());
        commentResponseDto.setCreated(comment.getCreated());
        commentResponseDto.setAuthorName(comment.getAuthor().getName());
        return commentResponseDto;
    }

    List<ItemRequestIdResponseDto> mapItemOwner(List<Item> items);

    default ItemRequestIdResponseDto mapItemOwner(Item items) {
        return new ItemRequestIdResponseDto(items.getId(),
                items.getName(),
                items.getDescription(),
                items.getAvailable(),
                items.getRequest().getId());
    }

    @Mapping(target = "id", source = "booking.id")
    @Mapping(target = "bookerId", source = "booking.booker.id")
    LastNextItemResponseDto toLastNextItemDto(Booking booking);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "lastBooking", source = "lastBooking")
    @Mapping(target = "nextBooking", source = "nextBooking")
    @Mapping(target = "comments", source = "comments")
    ItemBookingDto toItemBookingCommentDto(Item item,
                                           LastNextItemResponseDto lastBooking,
                                           LastNextItemResponseDto nextBooking,
                                           Long id,
                                           List<CommentResponseDto> comments);
}
