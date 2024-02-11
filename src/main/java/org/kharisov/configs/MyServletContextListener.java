package org.kharisov.configs;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;
import org.kharisov.repos.databaseImpls.*;
import org.kharisov.services.databaseImpls.*;
import org.kharisov.services.interfaces.*;
import org.kharisov.services.singletons.*;

/**
 * Класс MyServletContextListener реализует интерфейс ServletContextListener и
 * используется для инициализации различных сервисов при старте веб-приложения.
 */
@WebListener
public class MyServletContextListener implements ServletContextListener {

    /**
     * Метод contextInitialized вызывается при старте веб-приложения.
     * Он инициализирует различные сервисы и добавляет их в контекст сервлета.
     *
     * @param sce событие контекста сервлета.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ServletContext ctx = sce.getServletContext();
        ConnectionPoolConfig config = new ConnectionPoolConfig();

        ConnectionPool connectionPool = new ConnectionPool(config);
        AuthService authService = new AuthDbService(new UserDbRepo(connectionPool), new RoleDbRepo(connectionPool));
        ctx.setAttribute("authService", authService);

        ConnectionPool connectionPool2 = new ConnectionPool(config);
        ReadingService readingService = new ReadingDbService(
                new ReadingDbRepo(connectionPool2),
                new UserDbRepo(connectionPool2),
                new ReadingTypeDbRepo(connectionPool2));
        ctx.setAttribute("readingService", readingService);

        ConnectionPool connectionPool3 = new ConnectionPool(config);
        AuditService auditService = new AuditDbService(
                new UserDbRepo(connectionPool3),
                new AuditDbRepo(connectionPool3)
        );
        ctx.setAttribute("auditService", auditService);
        AuditServiceSingleton.initialize(ctx);

        ConnectionPool connectionPool4 = new ConnectionPool(config);
        ReadingTypeService readingTypeService = new ReadingTypeDbService(new ReadingTypeDbRepo(connectionPool4));
        ctx.setAttribute("readingTypeService", readingTypeService);
        ReadingTypeServiceSingleton.initialize(ctx);
    }
}
