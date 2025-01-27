package com.pet.logines.repositories;

import com.pet.logines.models.entities.PhoneLogin;
import com.pet.logines.models.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PhoneLoginRepository extends JpaRepository<PhoneLogin, Long> {
    @Query("SELECT p FROM PhoneLogin p WHERE p.phoneNumber = :phoneNumber AND p.user.status = :status")
    List<PhoneLogin> findByPhoneNumberAndUserStatus(String phoneNumber, UserStatus status);
    Optional<PhoneLogin> findByUserId(Long userId);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END FROM PhoneLogin p WHERE p.phoneNumber = :phoneNumber AND p.user.status = 'ACTIVE'")
    boolean existsActiveUserByPhoneNumber(String phoneNumber);
}
