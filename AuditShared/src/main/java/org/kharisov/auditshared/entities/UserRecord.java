package org.kharisov.auditshared.entities;

/**
 * Сущность User представляет собой пользователя в системе.
 * Эта сущность содержит id, номер счета, пароль и id роли.
 */
public record UserRecord (Long id, String accountNum, String password, String role){

}

