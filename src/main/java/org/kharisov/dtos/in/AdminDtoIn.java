package org.kharisov.dtos.in;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminDtoIn {
    @NotNull
    @Size(min = 16, max = 16, message = "Field must be exactly 16 characters")
    private String accountNum;

}
