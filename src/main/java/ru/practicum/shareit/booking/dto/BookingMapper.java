package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "itemId", source = "itemId")
    BookingDto toBookingDto(Booking booking, Long itemId);
    @Mapping(target = "status", defaultValue = "WAITING")
    @Mapping(target = "bookerId", source = "userId")
    Booking toBooking(BookingDto bookingDto, Long userId);
    BookingResponseDto bookingToBookingResponseDto(Booking booking);


}
