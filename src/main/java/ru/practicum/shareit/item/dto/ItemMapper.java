package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;

/*@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : 0,
                item.getUserId()
        );
    }

    public static Item toItem(ItemDto itemDto, long userId, long itemId) {
        return new Item(
                itemId,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                itemDto.getRequest() != 0 ? new ItemRequest() : null,
                userId
        );
    }
}*/
@Mapper(componentModel = "spring")
public interface ItemMapper {
    //@Mapping(target = "request", source = "item.getRequest().getId()")
    ItemDto toItemDto(Item item);
    @Mapping(target = "id", source = "itemId")
    @Mapping(target = "owner", source = "userId")
    //@Mapping(target = "request", source = "null")
    Item toItem(ItemDto itemDto, long userId, long itemId);
}
