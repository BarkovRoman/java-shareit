package ru.practicum.shareit.booking.repositry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.LastNextItemShortDto;

import java.util.LinkedList;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerIdOrderByEndDesc(Long userId);

    @Query("select b from Booking b  left join Item i on b.item.id = i.id where i.owner = ?1 order by b.end desc ")
    List<Booking> findByOwnerId(Long userId);

    @Query("select b from Booking b left join Item i on b.item.id = i.id where i.id = ?1 and i.owner = ?2 order by b.end asc ")
    LinkedList<LastNextItemShortDto> getBookingByItem(Long itemId, Long userId);
}