package org.kharisov.exceptions;

/**
 * Класс исключения, представляющий конфликтные ситуации.
 * Это пользовательское исключение, которое расширяет RuntimeException.
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
