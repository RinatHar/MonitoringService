package org.kharisov.exceptions;

/**
 * Класс исключения, представляющий ситуации, связанные с ошибками базы данных.
 */
public class MyDatabaseException extends RuntimeException {
    public MyDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
