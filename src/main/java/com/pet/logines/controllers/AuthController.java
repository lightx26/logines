package com.pet.logines.controllers;

import com.pet.logines.dtos.requests.PhoneLoginParams;
import com.pet.logines.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid PhoneLoginParams phoneLoginParams) {
        authService.register(phoneLoginParams);
        return ResponseEntity.ok().build();
    }
}
