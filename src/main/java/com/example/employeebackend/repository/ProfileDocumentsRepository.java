package com.example.employeebackend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employeebackend.entity.Profile;
import com.example.employeebackend.entity.ProfileDocuments;

public interface ProfileDocumentsRepository
        extends JpaRepository<ProfileDocuments, Long> {

    Optional<ProfileDocuments> findByProfile(Profile profile);
}