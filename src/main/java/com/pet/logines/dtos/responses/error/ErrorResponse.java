package com.pet.logines.dtos.responses.error;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ErrorResponse {
    private ErrorType error;
    private String message;
    private Instant timestamp;

    public ErrorResponse(ErrorType error, String message) {
        this.error = error;
        this.message = message;
        this.timestamp = Instant.now();
    }
}
