package com.onlinebookstore.config;

import com.onlinebookstore.commons.exceptions.BookIsNotAvailableException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookIsNotAvailableException.class)
    public ResponseEntity<String> bookIsNotAvailableException(BookIsNotAvailableException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Книга, яку Ви бажаєте придбати, на жаль, недоступна");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> runtimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Сталася непередбачувана помилка");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgumentWhenBuying(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Кількість книг, яку ви бажаєте придбати, вище за доступну.");
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>("Помилка в отриманні об'єкту з бази даних! Об'єкт не знайдено за Вашим запитом", HttpStatus.NOT_FOUND);
    }

}
