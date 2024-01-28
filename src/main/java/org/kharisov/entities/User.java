package org.kharisov.entities;

import lombok.*;
import java.util.*;

/**
 * Сущность User представляет собой пользователя в системе.
 * Эта сущность содержит номер счета, пароль, список показаний и флаг администратора.
 */
@Getter
@Setter
@Builder
public class User {
    /**
     * Номер счета пользователя.
     */
    private String accountNum;

    /**
     * Пароль пользователя.
     */
    private String password;

    /**
     * Список показаний пользователя.
     */
    @Builder.Default
    private List<ReadingRecord> readings = new ArrayList<>();

    /**
     * Флаг, указывающий, является ли пользователь администратором.
     */
    @Builder.Default
    private boolean isAdmin = false;

    /**
     * Проверяет, является ли пользователь администратором.
     * @return true, если пользователь является администратором, иначе false.
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * Переопределение метода equals для сравнения объектов User.
     * @param obj Объект, с которым сравнивается текущий объект.
     * @return true, если объекты равны, иначе false.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return isAdmin == user.isAdmin &&
                Objects.equals(accountNum, user.accountNum) &&
                Objects.equals(password, user.password) &&
                Objects.equals(readings, user.readings);
    }

    /**
     * Переопределение метода hashCode для получения хеш-кода объекта User по всем полям.
     * @return Хеш-код объекта.
     */
    @Override
    public int hashCode() {
        return Objects.hash(accountNum, password, readings, isAdmin);
    }
}

