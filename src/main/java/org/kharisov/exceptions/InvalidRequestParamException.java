package org.kharisov.exceptions;

public class InvalidRequestParamException extends RuntimeException {
    public InvalidRequestParamException(String message) {
        super(message);
    }
}
