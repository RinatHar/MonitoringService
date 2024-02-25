package org.kharisov.entities;

public record UserAuditRecord(Long id, String action, String accountNum) {
}
