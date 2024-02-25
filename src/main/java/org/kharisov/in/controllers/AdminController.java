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

/**
 * Контроллер для работы с административными функциями.
 */
@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;
    private final AuditService auditService;
    private final ReadingTypeService readingTypeService;
    private final ReadingService readingService;

    /**
     * Получить все записи аудита.
     *
     * @return Список записей аудита.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/audit")
    public ResponseEntity<Map<String, List<String>>> getAllAuditRecords() {
        Map<String, List<String>> allAudits = auditService.getAllAuditRecords();
        return ResponseEntity.ok(allAudits);
    }

    /**
     * Получить все показания.
     *
     * @return Список всех показаний.
     */
    @GetMapping("/readings")
    public ResponseEntity<Map<String, List<ReadingDto>>> getAllReadings() {
        Map<String, List<ReadingDto>> allReadings = readingService.getAllReadings();
        return ResponseEntity.ok(allReadings);
    }

    /**
     * Назначить пользователя администратором.
     *
     * @param user DTO пользователя.
     * @return Статус операции.
     */
    @PostMapping("/makeAdmin")
    public ResponseEntity<String> makeAdmin(@RequestBody UserDto user) {
        DtoInValidator.validate(user, AccountNumValidationGroup.class);
        UserRecord newAdmin = UserMapper.INSTANCE.toEntity(user);
        authService.changeUserRole(newAdmin, Role.ADMIN);
        return ResponseEntity.ok("The user has successfully become an administrator");
    }

    /**
     * Добавить новый тип показания.
     *
     * @param readingTypeDto DTO типа показания.
     * @return Статус создания.
     */
    @PostMapping("/addReadingType")
    public ResponseEntity<String> addReadingType(@RequestBody ReadingTypeDto readingTypeDto) {
        DtoInValidator.validate(readingTypeDto);
        ReadingTypeRecord readingTypeRecord = ReadingTypeMapper.INSTANCE.toEntity(readingTypeDto);
        readingTypeService.addReadingType(readingTypeRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body("The type of reading has been added successfully");
    }
}
