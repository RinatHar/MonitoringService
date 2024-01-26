package org.kharisov.services;

import org.kharisov.entities.User;
import org.kharisov.repositories.AuditRepo;

import java.util.List;
import java.util.Map;

public class AuditService {
    private final AuditRepo auditMemoryRepo;

    public AuditService(AuditRepo auditMemoryRepo) {
        this.auditMemoryRepo = auditMemoryRepo;
    }

    public void logAction(User user, String action) {
        auditMemoryRepo.logAction(user.getAccountNum(), action);
    }

    public List<String> getLogs(User user) {
        return auditMemoryRepo.getLogs(user.getAccountNum());
    }

    public Map<String, List<String>> getAllLogs() {
        return auditMemoryRepo.getAllLogs();
    }
}
