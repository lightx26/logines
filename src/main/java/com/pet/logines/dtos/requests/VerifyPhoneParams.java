package com.pet.logines.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyPhoneParams {
    @NotNull(message = "User ID is required")
    private Long userId;
    @NotBlank(message = "OTP is required")
    private String otp;
}
