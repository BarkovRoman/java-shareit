package ru.practicum.shareit.booking.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter @Setter @ToString
@AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "bookings", schema = "public")

public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime start;    // Дата и время начала бронирования
    private LocalDateTime end;    // Дата и время конца бронирования;
    private Long itemId;
    private Long bookerId;    // Пользователь, который осуществляет бронирование
    private BookingStatus status;
}
