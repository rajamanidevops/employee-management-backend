package com.example.employeebackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employeebackend.entity.Profile;
import com.example.employeebackend.entity.ProfilePersonal;

public interface ProfilePersonalRepository extends JpaRepository<ProfilePersonal, Long> {

    Optional<ProfilePersonal> findByProfile(Profile profile);
}