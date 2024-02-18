package org.kharisov.entities;

import lombok.*;

import java.time.LocalDate;

/**
 * Сущность IndicatorRecord представляет собой запись показания в системе.
 * Эта сущность содержит тип показания, его значение и дату.
 */

public record ReadingRecord(Long id, Long userId, Long typeId, int value, LocalDate date) {

}

