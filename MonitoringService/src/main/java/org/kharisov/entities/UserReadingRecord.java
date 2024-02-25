package org.kharisov.entities;

import java.time.LocalDate;

public record UserReadingRecord(Long id, String accountNum, String type, int value, LocalDate date) {
}
