package org.kharisov.repositories;

import java.util.List;
import java.util.Map;

public abstract class AuditRepo {
    public abstract void logAction(String accountNum, String action);
    public abstract List<String> getLogs(String accountNum);
    public abstract Map<String, List<String>> getAllLogs();
}
