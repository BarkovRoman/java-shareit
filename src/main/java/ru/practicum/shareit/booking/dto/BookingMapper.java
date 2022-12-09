package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE )
public interface BookingMapper {

    @Mapping(target = "status", defaultValue = "WAITING")
    @Mapping(target = "booker", source = "booker")
    @Mapping(target = "item", source = "item")
    @Mapping(target = "id", ignore = true)
    Booking toBooking(BookingDto bookingDto, Item item, User booker);
    //BookingDto toBookingDto(Booking booking, Long itemId);
    @Mapping(target = "item", source = "item")
    @Mapping(target = "id", source = "id")
    @Mapping(target = "booker", source = "booker")
    BookingResponseDto bookingToBookingResponseDto(Booking booking, ItemResponseDto item, BookerResponseDto booker,  Long id);

    ItemResponseDto itemResponseDto(Item item);
    BookerResponseDto bookerResponseDto(User booker);
}
