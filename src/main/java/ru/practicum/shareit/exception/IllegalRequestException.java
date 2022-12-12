package ru.practicum.shareit.exception;

public class IllegalRequestException extends RuntimeException{
    public IllegalRequestException(final String message) {
        super(message);
    }
}
