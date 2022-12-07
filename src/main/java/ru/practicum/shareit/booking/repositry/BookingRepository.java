package ru.practicum.shareit.booking.repositry;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
