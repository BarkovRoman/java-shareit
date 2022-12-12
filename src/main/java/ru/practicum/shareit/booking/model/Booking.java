package ru.practicum.shareit.booking.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor @NoArgsConstructor
@Entity
@Table(name = "bookings", schema = "public")

public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;    // Дата и время начала бронирования
    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;    // Дата и время конца бронирования;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id")
    User booker;
    @ManyToOne
    @JoinColumn(name = "item_id")
    Item item;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Booking)) return false;
        return id != null && id.equals(((Booking) o).getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}