package ru.practicum.shareit.booking.model;

public enum BookingStatus {
    WAITING,    // новое бронирование, ожидает одобрения
    APPROVED,    // бронирование подтверждено владельцем
    REJECTED,    // бронирование отклонено владельцем
    CANCELED,    // бронирование отменено создателем
    CURRENT,    // текущие
    PAST,     // завершённые
    FUTURE,   // будущие
    ALL    // Все
}
