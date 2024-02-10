package org.kharisov.configs;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;
import org.kharisov.repos.databaseImpls.*;
import org.kharisov.services.databaseImpls.AuthDbService;
import org.kharisov.services.interfaces.AuthService;

@WebListener
public class MyServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ConnectionPool connectionPool = new ConnectionPool(new ConnectionPoolConfig());
        AuthService authService = new AuthDbService(new UserDbRepo(connectionPool), new RoleDbRepo(connectionPool));
        sce.getServletContext().setAttribute("authService", authService);
    }
}
