package org.kharisov.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.kharisov.auditshared.entities.UserRecord;
import org.kharisov.configs.JwtProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;

/**
 * Класс JwtUtils предоставляет утилиты для работы с JSON Web Tokens (JWT).
 * Он содержит методы для генерации, валидации и извлечения информации из JWT.
 */
@Component
public class JwtUtils {

    private final String SECRET_KEY;
    private final Integer ACCESS_TOKEN_DURATION;
    private final Integer REFRESH_TOKEN_DURATION;
    private SecretKey secretKey;

    /**
     * Конструктор класса JwtUtils. Принимает секретный ключ и продолжительность действия токенов в качестве параметров.
     * Эти параметры используются для создания и валидации JWT.
     */
    @Autowired
    public JwtUtils(JwtProperties jwtProperties) {
        this.SECRET_KEY = jwtProperties.getSecretKey();
        this.ACCESS_TOKEN_DURATION = jwtProperties.getDurationAccess();
        this.REFRESH_TOKEN_DURATION = jwtProperties.getDurationRefresh();
        this.init();
    }

    /**
     * Метод init инициализирует секретный ключ, который будет использоваться для подписи JWT.
     */
    public void init() {
        secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * Извлекает JWT из запроса.
     *
     * @param req Запрос, из которого извлекается JWT
     * @return JWT в виде строки или null, если JWT не найден
     */
    public String extractJwtFromRequest(HttpServletRequest req) {
        String authHeader = req.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // The part after "Bearer "
        }

        return null;
    }

    /**
     * Метод extractUserId извлекает идентификатор пользователя из JWT.
     * Идентификатор пользователя хранится в поле "subject" JWT.
     *
     * @param token JWT, из которого извлекается идентификатор пользователя.
     * @return Идентификатор пользователя в виде Long.
     */
    public Long extractUserId(String token) {
        return Long.parseLong(Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    /**
     * Метод extractExpiration извлекает дату истечения срока действия JWT.
     * Дата истечения срока действия хранится в поле "exp" JWT.
     *
     * @param token JWT, из которого извлекается дата истечения срока действия.
     * @return Дата истечения срока действия в виде Date.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Метод extractClaim извлекает определенное утверждение из JWT.
     *
     * @param token JWT, из которого извлекается утверждение.
     * @param claimsResolver Функция, которая извлекает утверждение из набора утверждений JWT.
     * @return Утверждение из JWT.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Метод extractAllClaims извлекает все утверждения из JWT.
     *
     * @param token JWT, из которого извлекаются утверждения.
     * @return Набор всех утверждений JWT.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Метод isTokenValid проверяет, действителен ли JWT.
     * JWT считается действительным, если его подпись верна и он не истек.
     *
     * @param token JWT, который проверяется на действительность.
     * @return true, если JWT действителен, иначе false.
     */
    public Boolean isTokenValid(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    /**
     * Метод generateTokens генерирует токены доступа и обновления для пользователя.
     *
     * @param user Пользователь, для которого генерируются токены.
     * @return Map с токенами доступа и обновления.
     */
    public Map<String, String> generateTokens(UserRecord user) {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    /**
     * Метод generateAccessToken генерирует токен доступа для пользователя.
     *
     * @param user Пользователь, для которого генерируется токен доступа.
     * @return Токен доступа в виде строки.
     */
    public String generateAccessToken(UserRecord user) {
        Map<String, Object> claims = new HashMap<>();
        Duration duration = Duration.ofMinutes(ACCESS_TOKEN_DURATION);
        return createToken(claims, String.valueOf(user.id()), duration);
    }

    /**
     * Метод generateRefreshToken генерирует токен обновления для пользователя.
     *
     * @param user Пользователь, для которого генерируется токен обновления.
     * @return Токен обновления в виде строки.
     */
    public String generateRefreshToken(UserRecord user) {
        Map<String, Object> claims = new HashMap<>();
        Duration duration = Duration.ofMinutes(REFRESH_TOKEN_DURATION);
        return createToken(claims, String.valueOf(user.id()), duration);
    }

    /**
     * Метод createToken создает новый JWT.
     *
     * @param claims Набор утверждений JWT, которые должны быть включены в токен.
     * @param subject Субъект JWT, обычно идентификатор пользователя.
     * @param duration Продолжительность действия JWT.
     * @return JWT в виде строки.
     */
    private String createToken(Map<String, Object> claims, String subject, Duration duration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + duration.toMillis()))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Метод validateToken проверяет, действителен ли токен для указанного идентификатора пользователя.
     *
     * @param token JWT, который проверяется на действительность.
     * @param userId Идентификатор пользователя, для которого проверяется токен.
     * @return true, если JWT действителен для указанного пользователя, иначе false.
     */
    public Boolean validateToken(String token, Long userId) {
        final Long id = extractUserId(token);
        return (id.equals(userId) && isTokenValid(token));
    }
}
