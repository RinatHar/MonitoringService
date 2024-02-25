package org.kharisov.exceptions;

/**
 * Класс исключения, представляющий ситуации, когда переданный параметр запроса недействителен.
 */
public class InvalidRequestParamException extends RuntimeException {
    public InvalidRequestParamException(String message) {
        super(message);
    }
}
