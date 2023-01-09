package ru.practicum.shareit.exception;

public class ExistingValidationException extends RuntimeException {
    public ExistingValidationException(final String message) {
        super(message);
    }
}
