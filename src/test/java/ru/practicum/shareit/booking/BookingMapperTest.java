package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.time.LocalDateTime;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingMapperTest {

    BookingMapper mapper;

    @Test
    public void bookingToBookingResponseDto {
        Booking booking = new Booking(1L,
                LocalDateTime.now(),
                LocalDateTime.now().plusSeconds(2),
                BookingStatus.APPROVED,
                new User(1L, "Name", "name@email.ru"),
                new Item(1L, "ItemName", "ItemDescription", true,
                        new ItemRequest(1L, "ItemRequestDescriion",
                                new User(1L, "Name", "name@email.ru"), LocalDateTime.now()),
                                1L));

        BookingResponseDto bookingResponseDto = mapper.bookingToBookingResponseDto(booking);
        assertThat(BookingStatus.APPROVED, equalTo(booking.getStatus()));



}
