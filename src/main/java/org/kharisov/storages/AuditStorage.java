package org.kharisov.storages;

import java.util.ArrayList;
import java.util.HashMap;

public class AuditStorage {
    private HashMap<String, ArrayList<String>> auditStorage = new HashMap<>();

    public AuditStorage() {
    }

    public HashMap<String, ArrayList<String>> getAuditStorage() {
        return auditStorage;
    }
}
