package org.kharisov.loggingstarter.annotations;

import java.lang.annotation.*;

/**
 * Аннотация Loggable используется для отметки методов, которые должны быть залогированы.
 * Она может быть применена к любому методу для автоматического логирования вызовов этого метода.
 *
 * @see java.lang.annotation.Retention
 * @see java.lang.annotation.Target
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Loggable {
}
