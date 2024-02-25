package org.kharisov.annotations;

import java.lang.annotation.*;

/**
 * Аннотация Audit используется для отметки методов, которые должны быть проанализированы.
 * Она содержит один элемент - 'action', который описывает действие, выполняемое методом.
 *
 * @see java.lang.annotation.Retention
 * @see java.lang.annotation.Target
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Audit {
    /**
     * Описывает действие, выполняемое методом.
     *
     * @return строка, описывающая действие.
     */
    String action();
}
