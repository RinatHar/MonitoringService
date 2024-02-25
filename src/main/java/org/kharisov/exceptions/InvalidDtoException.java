package org.kharisov.exceptions;

/**
 * Класс исключения, представляющий ситуации, когда переданный DTO недействителен.
 */
public class InvalidDtoException extends RuntimeException {
    public InvalidDtoException(String message) {
        super(message);
    }
}
