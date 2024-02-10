package org.kharisov.in.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import org.kharisov.utils.AuthUtils;

import java.io.IOException;

@WebFilter(urlPatterns = {"/readings/*"})
public class JwtFilter implements Filter {

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
