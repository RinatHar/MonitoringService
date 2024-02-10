package org.kharisov.dtos.in;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.kharisov.annotations.*;

@Getter
@Setter
public class ReadingDtoIn {
    @NotNull
    @ReadingTypeExists
    private String readingType;

    @NotNull
    @IsInteger
    private String value;
}
