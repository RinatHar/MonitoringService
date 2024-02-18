package org.kharisov.in.filters;

import javax.servlet.*;
import javax.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.kharisov.entities.UserRecord;
import org.kharisov.exceptions.MyDatabaseException;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.utils.JwtUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

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
@RequiredArgsConstructor
public class JwtFilter implements Filter {

    private final AuthService authService;
    private final JwtUtils jwtUtils;

    /**
     * Метод doFilter обрабатывает входящий запрос и выполняет серию проверок JWT.
     * Если все проверки пройдены, управление передается следующему элементу в цепочке фильтров.
     *
     * @param request  входящий запрос
     * @param response ответ
     * @param chain    цепочка фильтров
     * @throws IOException      в случае ошибки ввода-вывода
     * @throws ServletException в случае ошибки сервлета
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String jwt = jwtUtils.extractJwtFromRequest(req);

        if (jwt == null) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Authorization header.");
            return;
        }

        if (!jwtUtils.isTokenValid(jwt)) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token.");
            return;
        }

        Long userId = jwtUtils.extractUserId(jwt);
        Optional<UserRecord> userRecordOptional = getUser(userId);
        if (userRecordOptional.isEmpty()) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token.");
            return;
        }

        authenticateUser(req, jwt, userRecordOptional.get());

        chain.doFilter(request, response);
    }

    private Optional<UserRecord> getUser(Long userId) {
        try {
            return authService.getUserById(userId);
        } catch (MyDatabaseException e) {
            return Optional.empty();
        }
    }

    private void authenticateUser(HttpServletRequest request, String jwt, UserRecord user) {
        if (jwtUtils.validateToken(jwt, user.id())) {

            String role = "ROLE_" + String.valueOf(authService.getRoleById(user.role_id())).toUpperCase();

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
