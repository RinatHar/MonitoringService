package org.kharisov.in.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import org.kharisov.utils.AuthUtils;

import java.io.IOException;

/**
 * Фильтр JwtFilter реализует интерфейс Filter.
 *
 * <p>Этот фильтр выполняет следующие действия:</p>
 * <ul>
 *   <li>Извлекает JWT из запроса.</li>
 *   <li>Проверяет, является ли JWT null. Если да, то отправляет ошибку 401 с сообщением "Invalid Authorization header".</li>
 *   <li>Извлекает субъект из JWT. Если субъект null, отправляет ошибку 401 с сообщением "Invalid token".</li>
 *   <li>Извлекает роль из JWT. Если роль null или не равна "USER" или "ADMIN", отправляет ошибку 401 с сообщением "Invalid token or role".</li>
 *   <li>Если все проверки пройдены, передает управление следующему элементу в цепочке фильтров.</li>
 * </ul>
 */
@WebFilter(urlPatterns = {"/api/v1/readings/*", "/api/v1/admin/*"})
public class JwtFilter implements Filter {

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

        String jwt = AuthUtils.extractJwtFromRequest(req);

        if (jwt == null) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Authorization header.");
            return;
        }

        String subject = AuthUtils.getSubjectFromJwt(jwt);

        if (subject == null) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token.");
            return;
        }

        String role = AuthUtils.getRoleFromJwt(jwt);

        if (role == null || (!role.equals("USER") && !role.equals("ADMIN"))) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token or role.");
            return;
        }

        chain.doFilter(request, response);
    }
}
