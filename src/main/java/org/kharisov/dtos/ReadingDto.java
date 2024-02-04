package org.kharisov.dtos;

import lombok.Getter;
import lombok.Setter;
import org.kharisov.entities.ReadingType;

import java.time.LocalDate;

@Getter
@Setter
public class ReadingDto {
    private Long id;
    private Long userId;
    private String accountNum;
    private ReadingType type;
    private Long typeId;
    private int value;
    private LocalDate date;
}
