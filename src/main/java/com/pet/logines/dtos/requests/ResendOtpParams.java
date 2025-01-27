package com.pet.logines.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResendOtpParams {
    @NotNull(message = "User ID is required")
    private Long userId;
}
