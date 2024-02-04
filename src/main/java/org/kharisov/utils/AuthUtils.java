package org.kharisov.utils;

import org.kharisov.entities.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class AuthUtils {
    /**
     * Метод для хеширования пароля с использованием соли.
     * @param password Пароль, который нужно захешировать.
     * @return Хеш пароля, сгенерированный с использованием соли. Если произошла ошибка, возвращает исходный пароль.
     */
    public static String hashPassword(String password) {
        try {
            // Генерируем соль
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            // Добавляем соль к паролю
            String saltedPassword = Base64.getEncoder().encodeToString(salt) + password;

            // Хешируем пароль с солью
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(saltedPassword.getBytes());

            // Возвращаем хеш пароля вместе с солью
            return Base64.getEncoder().encodeToString(salt) + "$" + Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException ex) {
            return password;
        }
    }

    /**
     * Метод для проверки пароля пользователя.
     * @param password Введенный пароль.
     * @param storedPasswordHash Хеш сохраненного пароля.
     * @return true, если введенный пароль соответствует сохраненному хешу пароля, иначе false.
     */
    public static boolean checkPassword(String password, String storedPasswordHash) {
        try {
            // Извлекаем соль и хеш из сохраненного значения
            String[] parts = storedPasswordHash.split("\\$");
            String salt = parts[0];
            String storedHash = parts[1];

            // Добавляем соль к введенному паролю и хешируем его
            String saltedPassword = salt + password;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(saltedPassword.getBytes());

            // Сравниваем полученный хеш с сохраненным хешем
            return Base64.getEncoder().encodeToString(hash).equals(storedHash);
        } catch (NoSuchAlgorithmException ex) {
            return false;
        }
    }

    public static boolean isValid(User user) {
        return (user.getAccountNum().length() == 16
                && user.getAccountNum().matches("\\d+")
                && user.getPassword().length() > 7);
    }
}
