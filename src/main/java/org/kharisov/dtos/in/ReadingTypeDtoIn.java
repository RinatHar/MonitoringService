package org.kharisov.dtos.in;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadingTypeDtoIn {
    @NotNull
    private String name;
}
