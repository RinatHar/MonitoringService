package org.kharisov.storages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuditStorageMemory {
    private final Map<String, List<String>> auditStorage = new HashMap<>();

    public AuditStorageMemory() {
    }

    public Map<String, List<String>> getStorage() {
        return auditStorage;
    }
}
