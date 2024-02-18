package org.kharisov.entities;


public record AuditRecord(Long id, String action, Long userId) { }
