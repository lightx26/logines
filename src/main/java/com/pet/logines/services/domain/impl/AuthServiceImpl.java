package com.pet.logines.services.domain.impl;

import com.pet.logines.dtos.requests.PhoneLoginParams;
import com.pet.logines.dtos.responses.auth.RegisterResponse;
import com.pet.logines.models.entities.PhoneLogin;
import com.pet.logines.models.entities.User;
import com.pet.logines.models.enums.UserStatus;
import com.pet.logines.repositories.PhoneLoginRepository;
import com.pet.logines.repositories.UserRepository;
import com.pet.logines.services.domain.AuthService;
import com.pet.logines.services.domain.UserService;
import com.pet.logines.services.external.RedisService;
import com.pet.logines.services.external.SmsService;
import com.pet.logines.utilities.OtpGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PhoneLoginRepository phoneLoginRepository;

    private final SmsService smsService;
    private final RedisService redisService;
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final String OTP_PREFIX = "OTP::";
    private final String OTP_COUNT_PREFIX = "OTP_COUNT::";
    private final int OTP_MAX_COUNT = 3;

    @Override
    public RegisterResponse registerWithPhone(@NonNull PhoneLoginParams params) {
        // Check if phone number already registered
        if (phoneLoginRepository.existsActiveUserByPhoneNumber(params.getPhoneNumber())) {
            throw new RuntimeException("Phone number already registered");
        }

        // If phone number not registered, register new user
        Long userId = registerNewUser(params);
        // Send OTP asynchronously
        CompletableFuture.runAsync(() -> sendOtp(params.getPhoneNumber()));

        return RegisterResponse.builder()
                .userId(userId.toString())
                .build();
    }

    private Long registerNewUser(@NonNull PhoneLoginParams params) {
        // Create new user
        User user = User.builder()
                .registeredAt(Instant.now())
                .status(UserStatus.INACTIVE)
                .build();
        User savedUser = userRepository.save(user);

        // Create new phone login for the user
        String hashedPassword = passwordEncoder.encode(params.getPassword());
        PhoneLogin phoneLogin = PhoneLogin.builder()
                .phoneNumber(params.getPhoneNumber())
                .password(hashedPassword)
                .user(savedUser)
                .build();
        phoneLoginRepository.save(phoneLogin);

        return savedUser.getId();
    }

    private void sendOtp(String phoneNumber) {
        if (isOtpLimitExceeded(phoneNumber)) {
            Long expire = redisService.getExpire(OTP_COUNT_PREFIX + phoneNumber);
            throw new RuntimeException("OTP limit exceeded" + (expire != null ? ", try again in " + expire + " seconds" : ""));
        }

        String otp = OtpGenerator.generateOTP();
        redisService.set(OTP_PREFIX + phoneNumber, otp, 60);
        redisService.increment(OTP_COUNT_PREFIX + phoneNumber, 300);
        smsService.sendSms(phoneNumber, "Your OTP is: " + otp);
    }

    private boolean isOtpLimitExceeded(String phoneNumber) {
        Integer count = redisService.get(OTP_COUNT_PREFIX + phoneNumber, Integer.class);
        return count != null && count >= OTP_MAX_COUNT;
    }

    @Override
    public void verifyOTP(Long userId, String otp) {
        PhoneLogin phoneLogin = phoneLoginRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Phone number not registered"));

        String phoneNumber = phoneLogin.getPhoneNumber();
        String savedOtp = redisService.get(OTP_PREFIX + phoneNumber);

        // Check if OTP is valid, if not delete OTP from Redis
        if (savedOtp == null || !savedOtp.equals(otp)) {
            redisService.delete(OTP_PREFIX + phoneNumber);
            throw new RuntimeException("Invalid OTP");
        }

        User user = phoneLogin.getUser();
        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new RuntimeException("Phone number already verified");
        }
        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);

        // Clean up inactive users asynchronously
        CompletableFuture.runAsync(() -> userService.cleanUpInactiveUsers(phoneNumber));
    }

    @Override
    public void resendOTP(Long userId) {
        PhoneLogin phoneLogin = phoneLoginRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Phone number not registered"));

        if (phoneLogin.getUser().getStatus() == UserStatus.ACTIVE) {
            throw new RuntimeException("Phone number already verified");
        }

        sendOtp(phoneLogin.getPhoneNumber());
    }
}
