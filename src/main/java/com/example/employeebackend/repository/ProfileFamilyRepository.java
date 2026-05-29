package com.example.employeebackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employeebackend.entity.Profile;
import com.example.employeebackend.entity.ProfileFamily;

public interface ProfileFamilyRepository
        extends JpaRepository<ProfileFamily, Long> {

    Optional<ProfileFamily> findByProfile(Profile profile);
}