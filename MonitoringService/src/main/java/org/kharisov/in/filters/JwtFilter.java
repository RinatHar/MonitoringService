package org.kharisov.in.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.kharisov.auditshared.entities.UserRecord;
import org.kharisov.exceptions.*;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.*;

/**
 * Фильтр JwtFilter реализует интерфейс Filter.
 *
 * <p>Этот фильтр выполняет следующие действия:</p>
 * <ul>
 *   <li>Извлекает JWT из запроса.</li>
 *   <li>Проверяет, является ли JWT null. Если да, то отправляет ошибку 401 с сообщением "Invalid Authorization header".</li>
 *   <li>Извлекает идентификатор пользователя из JWT. Если пользователя не существует, отправляет ошибку 401 с сообщением "Invalid token".</li>
 *   <li>Если все проверки пройдены, передает управление следующему элементу в цепочке фильтров.</li>
 * </ul>
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final AuthService authService;
    private final JwtUtils jwtUtils;

    @Autowired
    public JwtFilter(AuthService authService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = jwtUtils.extractJwtFromRequest(request);

        if (jwt != null && jwtUtils.isTokenValid(jwt)) {
            Long userId = jwtUtils.extractUserId(jwt);
            try {
                UserRecord userRecord = authService.getUserById(userId);
                authenticateUser(request, jwt, userRecord);
            } catch (EntityNotFoundException | MyDatabaseException e) {
                throw new UnauthorizedException("Invalid token");
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Аутентифицировать пользователя.
     *
     * @param request HttpServletRequest.
     * @param jwt JWT токен.
     * @param user UserRecord пользователя.
     */
    private void authenticateUser(HttpServletRequest request, String jwt, UserRecord user) {
        if (jwtUtils.validateToken(jwt, user.id())) {

            String role = "ROLE_" + user.role().toUpperCase();

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role));

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new
                    UsernamePasswordAuthenticationToken(user, null, authorities);
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }
}
