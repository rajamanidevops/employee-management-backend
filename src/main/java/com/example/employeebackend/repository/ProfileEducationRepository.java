package com.example.employeebackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employeebackend.entity.Profile;
import com.example.employeebackend.entity.ProfileEducation;

public interface ProfileEducationRepository
        extends JpaRepository<ProfileEducation, Long> {

    List<ProfileEducation> findByProfile(Profile profile);
}