package ru.practicum.shareit.booking.repositry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Page<Booking> findByBookerId(Long userId, PageRequest page);

    Page<Booking> findByBookerIdAndStatus(Long userId, BookingStatus status, PageRequest page);

    Page<Booking> findByBooker_IdAndStatusAndEndBefore(Long userId, BookingStatus status, LocalDateTime end, PageRequest page);

    @Query("select b from Booking b  left join Item i on b.item.id = i.id where i.owner = ?1 order by b.end desc ")
    Page<Booking> findByOwnerId(Long userId, PageRequest page);

    @Query("select b from Booking b  left join Item i on b.item.id = i.id where i.owner = ?1 and b.status = ?2 order by b.end desc ")
    Page<Booking> findByOwnerIdAndStatus(Long userId, BookingStatus status, PageRequest page);

    @Query("select b from Booking b left join Item i on b.item.id = i.id where i.id = ?1 and i.owner = ?2 and b.status = ?3 order by b.end asc ")
    List<Booking> getBookingByItem(Long itemId, Long userId, BookingStatus status);

    Boolean existsBookingByItem_IdAndBooker_IdAndStatusAndEndBefore(Long itemId, Long userId, BookingStatus status, LocalDateTime localDateTime);

    Booking findBookingByItem_IdAndBooker_IdAndStatusAndEndBefore(Long itemId, Long userId, BookingStatus approved, LocalDateTime now);

    @Query("select b from Booking b  left join Item i on b.item.id = i.id where i.owner = ?1 and b.status = ?2 and b.end < ?3 order by b.end desc ")
    Page<Booking> findByOwnerIdAndStatusIsBefore(Long userId, BookingStatus approved, LocalDateTime localDateTime, PageRequest page);

    List<Booking> findByItemInAndStatus(List<Item> items, BookingStatus status, Sort end);
}