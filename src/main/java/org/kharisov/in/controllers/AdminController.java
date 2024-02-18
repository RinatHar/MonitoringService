package org.kharisov.in.controllers;

import lombok.RequiredArgsConstructor;
import org.kharisov.dtos.*;
import org.kharisov.entities.*;
import org.kharisov.enums.Role;
import org.kharisov.mappers.*;
import org.kharisov.services.interfaces.*;
import org.kharisov.validators.DtoInValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("isAuthenticated() and hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;
    private final AuditService auditService;
    private final ReadingTypeService readingTypeService;
    private final ReadingService readingService;

    @GetMapping("/audit")
    public ResponseEntity<?> getAllAuditRecords() {
        Map<String, List<String>> allAudits = auditService.getAllAuditRecords();
        return ResponseEntity.ok(allAudits);
    }

    @GetMapping("/readings")
    public ResponseEntity<?> getAllReadings() {
        Map<String, List<UserReadingRecord>> allReadings = readingService.getAllReadings();
        return ResponseEntity.ok(allReadings);
    }

    @PostMapping("/makeAdmin")
    public ResponseEntity<?> makeAdmin(@RequestBody UserDto user) {
        DtoInValidator.validate(user);
        UserRecord newAdmin = UserMapper.INSTANCE.toEntity(user);
        authService.changeUserRole(newAdmin, Role.ADMIN);
        return ResponseEntity.ok("Пользователь успешно стал администратором");
    }

    @PostMapping("/addReadingType")
    public ResponseEntity<?> addReadingType(@RequestBody ReadingTypeDto readingTypeDto) {
        DtoInValidator.validate(readingTypeDto);
        ReadingTypeRecord readingTypeRecord = ReadingTypeMapper.INSTANCE.toEntity(readingTypeDto);
        readingTypeService.addReadingType(readingTypeRecord);
        return ResponseEntity.ok("Тип показания успешно добавлен");
    }
}
