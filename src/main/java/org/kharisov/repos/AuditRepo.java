package org.kharisov.repos;

import org.kharisov.entities.User;
import org.kharisov.storages.AuditStorage;

import java.util.ArrayList;

public class AuditRepo {
    private final AuditStorage auditStorage;

    public AuditRepo(AuditStorage auditStorage) {
        this.auditStorage = auditStorage;
    }

    public void logAction(User user, String action) {
        if (!auditStorage.getAuditStorage().containsKey(user.getAccountNum())) {
            auditStorage.getAuditStorage().put(user.getAccountNum(), new ArrayList<>());
        }
        auditStorage.getAuditStorage().get(user.getAccountNum()).add(action);
    }

    public ArrayList<String> getLogs(User user) {
        return auditStorage.getAuditStorage().get(user.getAccountNum());
    }
}
