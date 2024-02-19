package org.kharisov.exceptions;

import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

/**
 * Класс обработчика исключений, который обрабатывает различные типы исключений, возникающих в приложении.
 */
@RestControllerAdvice
public class MyExceptionHandler {

    /**
     * Обрабатывает исключения типа MyDatabaseException.
     * Возвращает ответ с кодом ошибки 500 и сообщением об ошибке.
     */
    @ExceptionHandler(MyDatabaseException.class)
    public ResponseEntity<String> handleMyDatabaseException(MyDatabaseException e) {
        System.out.println("[ERROR] " + e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while processing your request. Please try again later.");
    }

    /**
     * Обрабатывает исключения типа InvalidDtoException.
     * Возвращает ответ с кодом ошибки 400 и сообщением об ошибке.
     */
    @ExceptionHandler(InvalidDtoException.class)
    public ResponseEntity<String> handleInvalidDtoException(InvalidDtoException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    /**
     * Обрабатывает исключения типа InvalidRequestParamException.
     * Возвращает ответ с кодом ошибки 400 и сообщением об ошибке.
     */
    @ExceptionHandler(InvalidRequestParamException.class)
    public ResponseEntity<String> handleInvalidRequestParamException(InvalidRequestParamException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    /**
     * Обрабатывает исключения типа EntityNotFoundException.
     * Возвращает ответ с кодом ошибки 400 и сообщением об ошибке.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    /**
     * Обрабатывает исключения типа UnauthorizedException.
     * Возвращает ответ с кодом ошибки 401 и сообщением об ошибке.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    /**
     * Обрабатывает исключения типа AccessDeniedException.
     * Возвращает ответ с кодом ошибки 403 и сообщением об ошибке.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body("Insufficient user rights");
    }

    /**
     * Обрабатывает исключения типа ConflictException.
     * Возвращает ответ с кодом ошибки 409 и сообщением об ошибке.
     */
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<String> handleConflictException(ConflictException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }
}
