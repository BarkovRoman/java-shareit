package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.dto.ItemRequestIdResponseDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {
    @Mapping(target = "requestor", source = "requestor")
    @Mapping(target = "id", source = "itemRequestDto.id")
    ItemRequest toItemRequest(ItemRequestDto itemRequestDto, User requestor);
    @Mapping(target = "items", source = "items")
    ItemRequestResponseDto toItemRequestResponseDto(ItemRequest itemRequest, List<ItemRequestIdResponseDto> items);

    ItemRequestCreateDto toItemRequestCreateDto(ItemRequest itemRequest);

}
