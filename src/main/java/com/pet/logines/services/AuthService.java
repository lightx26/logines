package com.pet.logines.services;

import com.pet.logines.dtos.requests.PhoneLoginParams;

public interface AuthService {
    void register(PhoneLoginParams params);
}
