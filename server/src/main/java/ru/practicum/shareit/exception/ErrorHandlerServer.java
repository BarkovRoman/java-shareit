package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandlerServer {

    @ExceptionHandler
    public ResponseEntity<String> handleExistingValidationException(final ExistingValidationException e) {
        log.warn("Ошибка 400 {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(NotFoundException exception) {      // 404 NOT_FOUND
        Map<String, String> result = Map.of("Ошибка ", exception.getMessage());
        log.warn(String.valueOf(result), exception);
        return result;
    }

    @ExceptionHandler
    public ResponseEntity<String> handleIllegalRequestException(IllegalRequestException e) {   // 400 BAD_REQUEST
        log.warn("Ошибка 400 {}", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
