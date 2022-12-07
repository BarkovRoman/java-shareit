package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    @Mapping(target = "itemId", source = "itemId")
    @Mapping(target = "itemName", source = "itemName")
    BookingDto toBookingDto(Booking booking, Long itemId, String itemName);
    @Mapping(target = "status", defaultValue = "WAITING")
    @Mapping(target = "bookerId", source = "userId")
    Booking toBooking(BookingDto bookingDto, Long userId);
}
