package com.pet.logines.repositories;

import com.pet.logines.models.entities.PhoneLogin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneLoginRepository extends JpaRepository<PhoneLogin, Long> {
    Optional<PhoneLogin> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
}
