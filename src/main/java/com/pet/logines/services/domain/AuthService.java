package com.pet.logines.services.domain;

import com.pet.logines.dtos.requests.PhoneLoginParams;
import com.pet.logines.dtos.responses.auth.RegisterResponse;

public interface AuthService {
    RegisterResponse registerWithPhone(PhoneLoginParams params);
    void verifyOTP(Long userId, String otp);
    void resendOTP(Long userId);
}
