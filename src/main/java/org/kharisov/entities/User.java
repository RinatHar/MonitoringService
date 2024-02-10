package org.kharisov.entities;

import lombok.*;
import org.kharisov.enums.Role;

import java.util.*;

/**
 * Сущность User представляет собой пользователя в системе.
 * Эта сущность содержит номер счета, пароль, список показаний и флаг администратора.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
     * Флаг, указывающий, является ли пользователь администратором.
     */
    @Builder.Default
    private Role role = Role.USER;

    /**
     * Проверяет, является ли пользователь администратором.
     * @return true, если пользователь является администратором, иначе false.
     */
    public boolean isAdmin() {
        return role == Role.ADMIN;
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
        return role == user.role &&
                Objects.equals(accountNum, user.accountNum) &&
                Objects.equals(password, user.password);
    }

    /**
     * Переопределение метода hashCode для получения хеш-кода объекта User по всем полям.
     * @return Хеш-код объекта.
     */
    @Override
    public int hashCode() {
        return Objects.hash(accountNum, password, role);
    }
}

