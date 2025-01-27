package com.pet.logines.services.domain.impl;

import com.pet.logines.models.entities.PhoneLogin;
import com.pet.logines.models.enums.UserStatus;
import com.pet.logines.repositories.PhoneLoginRepository;
import com.pet.logines.repositories.UserRepository;
import com.pet.logines.services.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PhoneLoginRepository phoneLoginRepository;


    @Override
    public void cleanUpInactiveUsers(String phoneNumber) {
        List<PhoneLogin> listInactivePhoneLogin = phoneLoginRepository.findByPhoneNumberAndUserStatus(phoneNumber, UserStatus.INACTIVE);
        List<Long> userIds = listInactivePhoneLogin.stream()
                .map(phoneLogin -> phoneLogin.getUser().getId()).toList();

        phoneLoginRepository.deleteAllByIdInBatch(userIds);
        userRepository.deleteAllByIdInBatch(userIds);
    }
}
