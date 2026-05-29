package com.example.employeebackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employeebackend.entity.Profile;
import com.example.employeebackend.entity.ProfileBank;

public interface ProfileBankRepository
        extends JpaRepository<ProfileBank, Long> {

    Optional<ProfileBank> findByProfile(Profile profile);
}