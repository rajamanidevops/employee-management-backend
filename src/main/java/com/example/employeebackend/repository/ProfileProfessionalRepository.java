package com.example.employeebackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employeebackend.entity.Profile;
import com.example.employeebackend.entity.ProfileProfessional;

public interface ProfileProfessionalRepository
        extends JpaRepository<ProfileProfessional, Long> {

    List<ProfileProfessional> findByProfile(Profile profile);
}