package org.kharisov.in.controllers;

import lombok.RequiredArgsConstructor;
import org.kharisov.dtos.*;
import org.kharisov.entities.*;
import org.kharisov.enums.Role;
import org.kharisov.mappers.*;
import org.kharisov.services.interfaces.*;
import org.kharisov.validators.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;
    private final AuditService auditService;
    private final ReadingTypeService readingTypeService;
    private final ReadingService readingService;

    @GetMapping("/audit")
    public ResponseEntity<Map<String, List<String>>> getAllAuditRecords() {
        Map<String, List<String>> allAudits = auditService.getAllAuditRecords();
        return ResponseEntity.ok(allAudits);
    }

    @GetMapping("/readings")
    public ResponseEntity<Map<String, List<ReadingDto>>> getAllReadings() {
        Map<String, List<ReadingDto>> allReadings = readingService.getAllReadings();
        return ResponseEntity.ok(allReadings);
    }

    @PostMapping("/makeAdmin")
    public ResponseEntity<String> makeAdmin(@RequestBody UserDto user) {
        DtoInValidator.validate(user, AccountNumValidationGroup.class);
        UserRecord newAdmin = UserMapper.INSTANCE.toEntity(user);
        authService.changeUserRole(newAdmin, Role.ADMIN);
        return ResponseEntity.ok("The user has successfully become an administrator");
    }

    @PostMapping("/addReadingType")
    public ResponseEntity<String> addReadingType(@RequestBody ReadingTypeDto readingTypeDto) {
        DtoInValidator.validate(readingTypeDto);
        ReadingTypeRecord readingTypeRecord = ReadingTypeMapper.INSTANCE.toEntity(readingTypeDto);
        readingTypeService.addReadingType(readingTypeRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body("The type of reading has been added successfully");
    }
}
