package ru.practicum.shareit.exception;

public class ExistingEmailException extends RuntimeException {
    public ExistingEmailException(final String message) {
        super(message);
    }
}
