package org.kharisov.dtos.in;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenDtoIn {
    @NotNull(message = "Refresh token cannot be null")
    @Size(min = 100, max = 200, message = "Refresh token must be between 100 and 200 characters")
    private String refreshToken;
}
