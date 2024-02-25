package org.kharisov.exceptions;

/**
 * Класс исключения, представляющий ситуации, когда происходит неавторизованный доступ.
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
