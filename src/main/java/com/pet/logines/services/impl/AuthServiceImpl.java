package com.pet.logines.services.impl;

import com.pet.logines.dtos.requests.PhoneLoginParams;
import com.pet.logines.models.entities.PhoneLogin;
import com.pet.logines.models.entities.User;
import com.pet.logines.models.enums.UserStatus;
import com.pet.logines.repositories.PhoneLoginRepository;
import com.pet.logines.repositories.UserRepository;
import com.pet.logines.services.AuthService;
import com.pet.logines.services.external.RedisService;
import com.pet.logines.services.external.SmsService;
import com.pet.logines.utilities.OtpGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PhoneLoginRepository phoneLoginRepository;

    private final SmsService smsService;
    private final RedisService redisService;

    private final PasswordEncoder passwordEncoder;
    @Override
    public void register(PhoneLoginParams params) {
        Optional<PhoneLogin> phoneLogin = phoneLoginRepository.findByPhoneNumber(params.getPhoneNumber());

        if (phoneLogin.isPresent() && phoneLogin.get().getUser().getStatus() == UserStatus.ACTIVE) {
            throw new RuntimeException("Phone number already registered");
        }

        registerNewUser(params);
        CompletableFuture.runAsync(() -> sendOtp(params.getPhoneNumber()));
    }

    private void registerNewUser(PhoneLoginParams params) {
        User user = User.builder()
                .registeredAt(Instant.now())
                .status(UserStatus.INACTIVE)
                .build();
        User savedUser = userRepository.save(user);

        String hashedPassword = passwordEncoder.encode(params.getPassword());
        PhoneLogin phoneLogin = PhoneLogin.builder()
                .phoneNumber(params.getPhoneNumber())
                .password(hashedPassword)
                .user(savedUser)
                .build();
        phoneLoginRepository.save(phoneLogin);
    }

    private void sendOtp(String phoneNumber) {
        // Send OTP to phone number
        String otp = OtpGenerator.generateOTP();
        smsService.sendSms(phoneNumber, "Your OTP is: " + otp);
        redisService.set(phoneNumber, otp, 60);
    }
}
