package com.pet.logines.controllers;

import com.pet.logines.dtos.requests.GoogleLoginParams;
import com.pet.logines.dtos.requests.PhoneLoginParams;
import com.pet.logines.dtos.requests.ResendOtpParams;
import com.pet.logines.dtos.requests.VerifyPhoneParams;
import com.pet.logines.services.domain.AuthService;
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
        return ResponseEntity.ok().body(
                authService.registerWithPhone(phoneLoginParams)
        );
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody @Valid VerifyPhoneParams verifyPhoneParams) {
        authService.verifyOTP(verifyPhoneParams.getUserId(), verifyPhoneParams.getOtp());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resend(@RequestBody @Valid ResendOtpParams resendOtpParams) {
        authService.resendOTP(resendOtpParams.getUserId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody @Valid GoogleLoginParams googleLoginParams) {
        return ResponseEntity.ok().body(
                authService.googleLogin(googleLoginParams.getIdToken())
        );
    }
}
