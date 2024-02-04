package org.kharisov.entities;

import lombok.*;

import java.time.LocalDate;

/**
 * Сущность IndicatorRecord представляет собой запись показания в системе.
 * Эта сущность содержит тип показания, его значение и дату.
 */
@Getter
@Setter
@Builder
public class ReadingRecord {
    /**
     * Счет пользователя.
     */
    private String accountNum;
    /**
     * Тип показания.
     */
    private ReadingType type;

    /**
     * Значение показания.
     */
    private int value;

    /**
     * Дата записи показания.
     */
    private LocalDate date;

    /**
     * Переопределение метода toString для представления сущности IndicatorRecord в виде строки.
     * @return строковое представление сущности IndicatorRecord.
     */
    @Override
    public String toString() {
        return  "Тип показания = " + type.getValue() + ", " +
                "значение = " + value + ", " +
                "дата = " + date;
    }
}

