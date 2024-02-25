package org.kharisov.exceptions;

/**
 * Класс исключения, представляющий ситуации, когда переданный DTO недействителен.
 */
public class InvalidUserException extends RuntimeException {
    public InvalidUserException(String message) {
        super(message);
    }
}
