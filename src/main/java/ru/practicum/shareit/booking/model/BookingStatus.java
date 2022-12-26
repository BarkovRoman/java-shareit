package ru.practicum.shareit.booking.model;

public enum BookingStatus {
    WAITING,    // новое бронирование, ожидает одобрения
    APPROVED,    // бронирование подтверждено владельцем
    REJECTED,    // бронирование отклонено владельцем
    PAST,     // завершённые
    All, ALL    // Все
}
