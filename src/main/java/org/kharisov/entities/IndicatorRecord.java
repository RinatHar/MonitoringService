package org.kharisov.entities;

import lombok.*;
import org.kharisov.storages.IndicatorType;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class IndicatorRecord {
    private IndicatorType type;
    private int value;
    private LocalDate date;

    @Override
    public String toString() {
        return "IndicatorRecord { " +
                "type = " + type.getValue() + ", " +
                "value = " + value + ", " +
                "date = " + date +
                " }";
    }
}