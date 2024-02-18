package org.kharisov.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.servlet.http.HttpServletRequest;
import org.kharisov.entities.UserRecord;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;

public class JwtUtils {

    private final String SECRET_KEY;
    private final Integer ACCESS_TOKEN_DURATION;
    private final Integer REFRESH_TOKEN_DURATION;
    private SecretKey secretKey;

    public JwtUtils(String secretKey, Integer accessTokenDuration, Integer refreshTokenDuration) {
        this.SECRET_KEY = secretKey;
        this.ACCESS_TOKEN_DURATION = accessTokenDuration;
        this.REFRESH_TOKEN_DURATION = refreshTokenDuration;
    }

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

    public Long extractUserId(String token) {
        return Long.parseLong(Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

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

    public Map<String, String> generateTokens(UserRecord user) {
        String accessToken = generateAccessToken(user);
        String refreshToken = generateRefreshToken(user);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    public String generateAccessToken(UserRecord user) {
        Map<String, Object> claims = new HashMap<>();
        Duration duration = Duration.ofMinutes(ACCESS_TOKEN_DURATION);
        return createToken(claims, String.valueOf(user.id()), duration);
    }

    public String generateRefreshToken(UserRecord user) {
        Map<String, Object> claims = new HashMap<>();
        Duration duration = Duration.ofMinutes(REFRESH_TOKEN_DURATION);
        return createToken(claims, String.valueOf(user.id()), duration);
    }

    private String createToken(Map<String, Object> claims, String subject, Duration duration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + duration.toMillis()))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean validateToken(String token, Long userId) {
        final Long id = extractUserId(token);
        return (id.equals(userId) && isTokenValid(token));
    }
}
