package org.kharisov.in.controllers;

import lombok.RequiredArgsConstructor;
import org.kharisov.dtos.*;
import org.kharisov.entities.*;
import org.kharisov.exceptions.InvalidRequestParamException;
import org.kharisov.mappers.ReadingMapper;
import org.kharisov.services.interfaces.*;
import org.kharisov.validators.DtoInValidator;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/readings")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@RequiredArgsConstructor
public class ReadingController {

    private final ReadingService readingService;
    private final ReadingTypeService readingTypeService;

    @GetMapping("/current")
    public ResponseEntity<ReadingDto> getCurrentReading(@RequestParam("type") String type) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRecord user = (UserRecord) auth.getPrincipal();
        ReadingDto reading = readingService.getCurrentReading(user, new ReadingTypeRecord(null, type));
        return ResponseEntity.ok(reading);
    }

    @GetMapping("/month")
    public ResponseEntity<List<ReadingDto>> getReadingsByMonth(@RequestParam("year") String year,
                                                            @RequestParam("month") String month) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRecord user = (UserRecord) auth.getPrincipal();
        int yearInt, monthInt;
        try {
            yearInt = Integer.parseInt(year);
            monthInt = Integer.parseInt(month);
        } catch (NumberFormatException e) {
            throw new InvalidRequestParamException("Incorrect year or month. They should be numbers.");
        }
        List<ReadingDto> readings = readingService.getReadingsByMonth(user, monthInt, yearInt);
        return ResponseEntity.ok(readings);
    }

    @GetMapping("/history")
    public ResponseEntity<List<ReadingDto>> getHistory() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRecord user = (UserRecord) auth.getPrincipal();
        List<ReadingDto> readings = readingService.getHistory(user);
        return ResponseEntity.ok(readings);
    }

    @PostMapping
    public ResponseEntity<String> addReading(@RequestBody ReadingDto readingDto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserRecord user = (UserRecord) auth.getPrincipal();

        DtoInValidator.validate(readingDto);

        ReadingTypeRecord readingTypeRecord = readingTypeService.getByName(readingDto.getType());
        ReadingRecord readingRecord = ReadingMapper.INSTANCE.toEntity(readingDto);
        readingService.addReading(user, readingTypeRecord, readingRecord.value());
        return ResponseEntity.status(HttpStatus.CREATED).body("The reading has been sent successfully");
    }
}
