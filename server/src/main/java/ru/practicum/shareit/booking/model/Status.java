package ru.practicum.shareit.booking.model;

public enum Status {
    CANCELED,    // бронирование отменено создателем
    CURRENT,    // текущие
    FUTURE,   // будущие
    REJECTED,
    APPROVED,
    PAST,
    WAITING,
    ALL
}
