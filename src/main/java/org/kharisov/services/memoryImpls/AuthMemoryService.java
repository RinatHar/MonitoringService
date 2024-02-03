package org.kharisov.services.memoryImpls;

import org.kharisov.entities.User;
import org.kharisov.repos.interfaces.UserRepo;
import org.kharisov.services.interfaces.AuthService;

import java.security.*;
import java.util.*;

/**
 * Класс AuthService представляет сервис для аутентификации и авторизации пользователей.
 * Этот класс предоставляет методы для проверки существования пользователя, добавления пользователя, входа в систему и проверки административных прав.
 */
public class AuthMemoryService implements AuthService {
    /**
     * Репозиторий для управления хранилищем пользователей.
     */
    private final UserRepo userRepo;

    /**
     * Конструктор класса AuthService.
     * @param userRepo Репозиторий для управления хранилищем пользователей.
     */
    public AuthMemoryService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Проверяет, существует ли пользователь с указанным номером счета.
     * @param accountNum Номер счета пользователя.
     * @return true, если пользователь существует, иначе false.
     */
    public boolean userExists(String accountNum) {
        User user = getUserByAccountNum(accountNum);
        return user != null;
    }

    /**
     * Получает пользователя по номеру счета.
     * @param accountNum Номер счета пользователя.
     * @return Объект User, если пользователь существует, иначе null.
     */
    public User getUserByAccountNum(String accountNum) {
        return userRepo.getUser(accountNum);
    }

    /**
     * Добавляет нового пользователя, если выполнены условия валидации.
     * @param user Объект User, представляющий нового пользователя.
     * @return Optional<User>, содержащий нового пользователя, если он был успешно добавлен, иначе Optional.empty().
     */
    public Optional<User> addUser(User user) {
        user.setAccountNum(user.getAccountNum().strip());
        if (user.getAccountNum().length() == 16
                && user.getAccountNum().matches("\\d+")
                && user.getPassword().length() > 7) {
            user.setPassword(hashPassword(user.getPassword()));
            return Optional.ofNullable(userRepo.addUser(user));
        }
        else {
            return Optional.empty();
        }
    }

    /**
     * Проверяет, может ли пользователь войти в систему с указанными номером счета и паролем.
     * @param accountNum Номер счета пользователя.
     * @param password Пароль пользователя.
     * @return true, если пользователь может войти в систему, иначе false.
     */
    public boolean logIn(String accountNum, String password) {
        User user = getUserByAccountNum(accountNum);
        return user != null && checkPassword(password, user.getPassword());
    }

    /**
     * Метод для хеширования пароля с использованием соли.
     * @param password Пароль, который нужно захешировать.
     * @return Хеш пароля, сгенерированный с использованием соли. Если произошла ошибка, возвращает исходный пароль.
     */
    public String hashPassword(String password) {
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
    public boolean checkPassword(String password, String storedPasswordHash) {
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

    /**
     * Проверяет, является ли пользователь с указанным номером счета администратором.
     * @param accountNum Номер счета пользователя.
     * @return true, если пользователь является администратором, иначе false.
     */
    public boolean isAdminByAccountNum(String accountNum) {
        return getUserByAccountNum(accountNum).isAdmin();
    }
}