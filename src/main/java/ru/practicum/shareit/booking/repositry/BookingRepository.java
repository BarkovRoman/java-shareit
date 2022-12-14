package ru.practicum.shareit.booking.repositry;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.LastNextItemShortDto;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBookerId(Long userId, Sort sort);

    List<Booking> findByBookerIdAndStatus(Long userId, BookingStatus status, Sort sort);

    List<Booking> findByBooker_IdAndStatusAndEndBefore(Long userId, BookingStatus status, LocalDateTime end, Sort sort);

    @Query("select b from Booking b  left join Item i on b.item.id = i.id where i.owner = ?1 order by b.end desc ")
    List<Booking> findByOwnerId(Long userId);

    @Query("select b from Booking b  left join Item i on b.item.id = i.id where i.owner = ?1 and b.status = ?2 order by b.end desc ")
    List<Booking> findByOwnerIdAndStatus(Long userId, BookingStatus status);

    @Query("select b from Booking b left join Item i on b.item.id = i.id where i.id = ?1 and i.owner = ?2 order by b.end asc ")
    LinkedList<LastNextItemShortDto> getBookingByItem(Long itemId, Long userId);

    Boolean existsBookingByItem_IdAndBooker_IdAndStatusAndEndBefore(Long itemId, Long userId, BookingStatus status, LocalDateTime localDateTime);

    Booking findBookingByItem_IdAndBooker_IdAndStatusAndEndBefore(Long itemId, Long userId, BookingStatus approved, LocalDateTime now);

    @Query("select b from Booking b  left join Item i on b.item.id = i.id where i.owner = ?1 and b.status = ?2 and b.end < ?3 order by b.end desc ")
    List<Booking> findByOwnerIdAndStatusIsBefore(Long userId, BookingStatus approved, LocalDateTime localDateTime);

    List<Booking> findByItemInAndStatus(List<Item> items, BookingStatus status, Sort end);
}