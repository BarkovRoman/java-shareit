package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
@AllArgsConstructor
public class ItemBookingDto {
    Long id;
    String name;
    String description;
    Boolean available;
    LastNextItemResponseDto lastBooking;
    LastNextItemResponseDto nextBooking;
    List<CommentResponseDto> comments;




}
