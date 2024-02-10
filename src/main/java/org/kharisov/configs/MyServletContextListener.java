package org.kharisov.configs;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;
import org.kharisov.repos.databaseImpls.*;
import org.kharisov.services.databaseImpls.AuditDbService;
import org.kharisov.services.databaseImpls.AuthDbService;
import org.kharisov.services.databaseImpls.ReadingDbService;
import org.kharisov.services.databaseImpls.ReadingTypeDbService;
import org.kharisov.services.interfaces.AuditService;
import org.kharisov.services.interfaces.AuthService;
import org.kharisov.services.interfaces.ReadingService;
import org.kharisov.services.interfaces.ReadingTypeService;
import org.kharisov.services.singletons.ReadingTypeServiceSingleton;

@WebListener
public class MyServletContextListener implements ServletContextListener {
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

        ConnectionPool connectionPool4 = new ConnectionPool(config);
        ReadingTypeService readingTypeService = new ReadingTypeDbService(new ReadingTypeDbRepo(connectionPool4));
        ctx.setAttribute("readingTypeService", readingTypeService);
        ReadingTypeServiceSingleton.initialize(ctx);
    }
}
