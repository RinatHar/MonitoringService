package org.kharisov.in.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.kharisov.auditshared.entities.UserRecord;
import org.kharisov.dtos.*;
import org.kharisov.entities.*;
import org.kharisov.mappers.*;
import org.kharisov.services.interfaces.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Контроллер для работы с показаниями.
 */
@RestController
@RequestMapping("/api/v1/readings")
@RequiredArgsConstructor
public class ReadingController {

    private final ReadingService readingService;
    private final ReadingTypeService readingTypeService;

    /**
     * Получить текущее показание для заданного типа.
     *
     * @param type Тип показания.
     * @return Текущее показание.
     */
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/current")
    public ResponseEntity<ReadingDto> getCurrentReading(@RequestParam("type") String type) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRecord user = (UserRecord) auth.getPrincipal();
        ReadingDto reading = readingService.getCurrentReading(user, new ReadingTypeRecord(null, type));
        return ResponseEntity.ok(reading);
    }

    /**
     * Получить показания за заданный месяц и год.
     *
     * @param year Год.
     * @param month Месяц.
     * @return Список показаний.
     */
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/month")
    public ResponseEntity<List<ReadingDto>> getReadingsByMonth(@RequestParam("year") int year,
                                                            @RequestParam("month") int month) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRecord user = (UserRecord) auth.getPrincipal();
        List<ReadingDto> readings = readingService.getReadingsByMonth(user, month, year);
        return ResponseEntity.ok(readings);
    }

    /**
     * Получить историю показаний.
     *
     * @return Список показаний.
     */
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/history")
    public ResponseEntity<List<ReadingDto>> getHistory() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRecord user = (UserRecord) auth.getPrincipal();
        List<ReadingDto> readings = readingService.getHistory(user);
        return ResponseEntity.ok(readings);
    }

    /**
     * Добавить новое показание.
     *
     * @param readingDto DTO показания.
     * @return Статус создания.
     */
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<String> addReading(@Valid @RequestBody ReadingDto readingDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRecord user = (UserRecord) auth.getPrincipal();

        ReadingTypeRecord readingTypeRecord = readingTypeService.getByName(readingDto.getType());
        ReadingRecord readingRecord = ReadingMapper.INSTANCE.toEntity(readingDto);
        readingService.addReading(user, readingTypeRecord, readingRecord.value());
        return ResponseEntity.status(HttpStatus.CREATED).body("The reading has been sent successfully");
    }

    /**
     * Получить все показания.
     *
     * @return Список всех показаний.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Map<String, List<ReadingDto>>> getAllReadings() {
        Map<String, List<ReadingDto>> allReadings = readingService.getAllReadings();
        return ResponseEntity.ok(allReadings);
    }

    /**
     * Добавить новый тип показания.
     *
     * @param readingTypeDto DTO типа показания.
     * @return Статус создания.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/addReadingType")
    public ResponseEntity<String> addReadingType(@Valid @RequestBody ReadingTypeDto readingTypeDto) {
        ReadingTypeRecord readingTypeRecord = ReadingTypeMapper.INSTANCE.toEntity(readingTypeDto);
        readingTypeService.addReadingType(readingTypeRecord);
        return ResponseEntity.status(HttpStatus.CREATED).body("The type of reading has been added successfully");
    }
}
