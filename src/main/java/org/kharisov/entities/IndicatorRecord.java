package org.kharisov.entities;

import lombok.*;
import org.kharisov.enums.IndicatorTypeEnum;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class IndicatorRecord {
    private IndicatorTypeEnum type;
    private int value;
    private LocalDate date;

    @Override
    public String toString() {
        return "IndicatorRecord{" +
                "type=" + type +
                ", value=" + value +
                ", date=" + date +
                '}';
    }
}