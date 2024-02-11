package org.kharisov.configs;

import org.kharisov.domains.User;

/**
 * Класс UserContextHolder используется для хранения и извлечения информации о пользователе в контексте текущего потока.
 * Он использует ThreadLocal для хранения объекта User, что позволяет изолировать данные пользователя для каждого потока.
 */
public class UserContextHolder {

    /**
     * ThreadLocal переменная для хранения объекта User.
     */
    private static final ThreadLocal<User> userHolder = new ThreadLocal<>();

    /**
     * Устанавливает объект User для текущего потока.
     *
     * @param user объект User, который нужно установить.
     */
    public static void setUser(User user) {
        userHolder.set(user);
    }

    /**
     * Возвращает объект User для текущего потока.
     *
     * @return объект User для текущего потока.
     */
    public static User getUser() {
        return userHolder.get();
    }

    /**
     * Удаляет объект User для текущего потока.
     */
    public static void clear() {
        userHolder.remove();
    }
}
