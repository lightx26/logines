package com.pet.logines.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleLoginParams {
    @NotBlank(message = "ID Token is required")
    private String idToken;
}
