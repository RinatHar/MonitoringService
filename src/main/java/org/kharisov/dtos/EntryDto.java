package org.kharisov.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntryDto {
    private Long id;
    private String action;
    private Long userId;
    private String accountNum;
}
