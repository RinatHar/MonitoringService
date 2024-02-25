package org.kharisov.exceptions;

import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

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
     * Обрабатывает исключения типа MethodArgumentTypeMismatchException.
     * Возвращает ответ с кодом ошибки 400 и сообщением об ошибке.
     */
    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<String> handleInvalidUserException(InvalidUserException e) {
        return ResponseEntity.badRequest().body("Invalid user data");
    }

    /**
     * Обрабатывает исключения типа MethodArgumentTypeMismatchException.
     * Возвращает ответ с кодом ошибки 400 и сообщением об ошибке.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(errorMessage);
    }

    /**
     * Обрабатывает исключения типа MethodArgumentTypeMismatchException.
     * Возвращает ответ с кодом ошибки 400 и сообщением об ошибке.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String error = "Parameter '" + e.getName() + "' should be of type '" + e.getRequiredType().getName() + "'";
        return ResponseEntity.badRequest().body(error);
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
