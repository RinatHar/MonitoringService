package org.kharisov.services.singletons;

import jakarta.servlet.ServletContext;
import org.kharisov.services.interfaces.AuditService;

public class AuditServiceSingleton {
    private static AuditService instance;

    private AuditServiceSingleton() {}

    public static void initialize(ServletContext context) {
        instance = (AuditService) context.getAttribute("auditService");
    }

    public static AuditService getInstance() {
        return instance;
    }
}
