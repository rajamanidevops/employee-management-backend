package com.example.employeebackend.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.employeebackend.entity.Profile;
import com.example.employeebackend.repository.ProfileRepository;

@Service
@Transactional
public class ProfileService {

    private final ProfileRepository repo;

    public ProfileService(ProfileRepository repo) {
        this.repo = repo;
    }

    // ===== CREATE NEW PROFILE =====
    public Profile createProfile(Profile profile) {
        // Ensure required fields are provided
        if (profile.getEmail() == null || profile.getEmail().isBlank()) {
            throw new RuntimeException("Email is required to create profile");
        }
        // You can add other validations here if needed (employeeId etc.)
        return repo.save(profile);
    }

    // ===== GET PROFILE BY EMAIL =====
    public Profile getProfile(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    // ===== GET OR CREATE PROFILE =====
    public Profile getOrCreateProfile(String email) {
        if (email == null || email.isBlank()) {
            throw new RuntimeException("Email is required");
        }

        return repo.findByEmail(email)
                .orElseGet(() -> {
                    Profile p = new Profile();
                    p.setEmail(email);
                    return repo.save(p);
                });
    }

    // ===== UPDATE BASIC DETAILS =====
    public Profile updateDetails(String email, Profile updated) {
        Profile p = getProfile(email); // use getProfile, not getOrCreate, to avoid empty row creation

        p.setName(updated.getName());
        p.setPhone(updated.getPhone());
        p.setAddress(updated.getAddress());
        p.setDesignation(updated.getDesignation());

        return repo.save(p);
    }

    // ===== UPLOAD PROFILE IMAGE =====
    public void uploadImage(String email, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Image file is required");
        }

        Profile p = getProfile(email);

        try {
            p.setProfileImage(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image file");
        }

        repo.save(p);
    }

    // ===== DELETE PROFILE IMAGE =====
    public void deleteImage(String email) {
        Profile p = getProfile(email);
        p.setProfileImage(null);
        repo.save(p);
    }
}