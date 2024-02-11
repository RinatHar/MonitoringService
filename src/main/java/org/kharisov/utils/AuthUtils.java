package org.kharisov.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.kharisov.configs.Config;
import org.kharisov.domains.User;

import java.security.*;
import java.util.*;

/**
 * Класс AuthUtils предоставляет утилиты для аутентификации и авторизации пользователей.
 *
 * <p>Этот класс содержит следующие методы:</p>
 * <ul>
 *   <li>hashPassword(String password): Хеширует пароль с использованием соли.</li>
 *   <li>checkPassword(String password, String storedPasswordHash): Проверяет, соответствует ли введенный пароль
 *   сохраненному хешу пароля.</li>
 *   <li>isValid(User user): Проверяет, является ли пользователь действительным.</li>
 *   <li>createJwtForUser(User user): Создает JWT для пользователя.</li>
 *   <li>extractJwtFromRequest(HttpServletRequest req): Извлекает JWT из запроса.</li>
 *   <li>getRoleFromJwt(String jwt): Получает роль пользователя из JWT.</li>
 *   <li>getSubjectFromJwt(String jwt): Получает субъект из JWT.</li>
 * </ul>
 */
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

    /**
     * Проверяет, является ли пользователь действительным.
     *
     * @param user Пользователь, который должен быть проверен.
     * @return true, если пользователь действителен, иначе false.
     * Пользователь считается действительным, если:
     * — его номер счета состоит из 16 цифр,
     * — его пароль содержит не менее 8 символов.
     */
    public static boolean isValid(User user) {
        return (user.getAccountNum().length() == 16
                && user.getAccountNum().matches("\\d+")
                && user.getPassword().length() > 7);
    }

    /**
     * Создает JWT (JSON Web Token) для пользователя.
     *
     * @param user Пользователь, для которого создается JWT
     * @return Map с accessToken и refreshToken
     */
    public static Map<String, String> createJwtForUser(User user) {
        String secretKey = Config.get("jwt.secretKey");
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        String accessToken = Jwts.builder()
                .setSubject(user.getAccountNum())
                .claim("role", user.getRole().toString())
                .setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 3600000L))
                .signWith(key)
                .compact();
        String refreshToken = Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(user.getAccountNum())
                .claim("role", user.getRole().toString())
                .setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + 86400000L))
                .signWith(key)
                .compact();
        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    /**
     * Извлекает JWT из запроса.
     *
     * @param req Запрос, из которого извлекается JWT
     * @return JWT в виде строки или null, если JWT не найден
     */
    public static String extractJwtFromRequest(HttpServletRequest req) {
        String authHeader = req.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // The part after "Bearer "
        }

        return null;
    }

    /**
     * Получает роль пользователя из JWT.
     *
     * @param jwt JWT, из которого извлекается роль пользователя
     * @return Роль пользователя в виде строки или null, если JWT недействителен
     */
    public static String getRoleFromJwt(String jwt) {
        String secretKey = Config.get("jwt.secretKey");
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        try {
            Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            return (jws.getBody()).get("role", String.class);
        } catch (JwtException var4) {
            return null;
        }
    }

    /**
     * Получает субъект из JWT.
     *
     * @param jwt JWT, из которого извлекается субъект
     * @return Субъект в виде строки или null, если JWT недействителен
     */
    public static String getSubjectFromJwt(String jwt) {
        String secretKey = Config.get("jwt.secretKey");
        Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        try {
            Jws<Claims> jws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            return jws.getBody().getSubject();
        } catch (JwtException var4) {
            return null;
        }
    }
}
