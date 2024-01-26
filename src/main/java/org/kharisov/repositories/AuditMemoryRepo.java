package org.kharisov.repositories;

import org.kharisov.storages.AuditStorageMemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AuditMemoryRepo extends AuditRepo{
    private final AuditStorageMemory auditStorage;

    public AuditMemoryRepo(AuditStorageMemory auditStorage) {
        this.auditStorage = auditStorage;
    }

    @Override
    public void logAction(String accountNum, String action) {
        if (!auditStorage.getStorage().containsKey(accountNum)) {
            auditStorage.getStorage().put(accountNum, new ArrayList<>());
        }
        auditStorage.getStorage().get(accountNum).add(action);
    }

    @Override
    public List<String> getLogs(String accountNum) {
        return auditStorage.getStorage().get(accountNum);
    }

    @Override
    public Map<String, List<String>> getAllLogs() {
        return auditStorage.getStorage();
    }
}
