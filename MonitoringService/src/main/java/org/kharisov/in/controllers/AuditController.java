package org.kharisov.in.controllers;

import lombok.RequiredArgsConstructor;
import org.kharisov.auditshared.services.interfaces.AuditService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
public class AuditController {

    private final AuditService auditService;

    /**
     * Получить все записи аудита.
     *
     * @return Список записей аудита.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Map<String, List<String>>> getAllAuditRecords() {
        Map<String, List<String>> allAudits = auditService.getAllAuditRecords();
        return ResponseEntity.ok(allAudits);
    }
}
