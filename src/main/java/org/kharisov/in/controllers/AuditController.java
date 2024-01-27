package org.kharisov.in.controllers;

import org.kharisov.entities.User;
import org.kharisov.services.AuditService;

import java.util.List;
import java.util.Map;

public class AuditController {
    private final AuditService auditService;

    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }

    public void logAction(User user, String action) {
        auditService.logAction(user, action);
    }

    public List<String> getLogs(User user) {
        return auditService.getLogs(user);
    }

    public Map<String, List<String>> getAllLogs() {
        return auditService.getAllLogs();
    }
}
