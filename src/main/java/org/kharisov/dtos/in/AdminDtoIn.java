package org.kharisov.dtos.in;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class AdminDtoIn {
    @NotNull
    @Size(min = 16, max = 16, message = "Field must be exactly 16 characters")
    private String accountNum;

}
