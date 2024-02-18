package org.kharisov.exceptions;

public class MyDatabaseException extends RuntimeException {
    public MyDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
