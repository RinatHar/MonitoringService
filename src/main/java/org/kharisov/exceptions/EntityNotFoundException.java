package org.kharisov.exceptions;

/**
 * Класс исключения, представляющий ситуации, когда сущность не найдена.
 * Это пользовательское исключение, которое расширяет RuntimeException.
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
