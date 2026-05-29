package com.example.employeebackend.service;

import org.springframework.stereotype.Service;

import com.example.employeebackend.entity.Profile;
import com.example.employeebackend.entity.ProfileFamily;
import com.example.employeebackend.repository.ProfileFamilyRepository;
import com.example.employeebackend.repository.ProfileRepository;

@Service
public class ProfileFamilyService {

    private final ProfileFamilyRepository familyRepository;
    private final ProfileRepository profileRepository;

    public ProfileFamilyService(ProfileFamilyRepository familyRepository,
                                ProfileRepository profileRepository) {
        this.familyRepository = familyRepository;
        this.profileRepository = profileRepository;
    }

    // ===== GET =====
    public ProfileFamily getByEmail(String email) {

        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return familyRepository.findByProfile(profile)
                .orElse(new ProfileFamily());
    }

    // ===== POST (CREATE) =====
    public ProfileFamily create(String email, ProfileFamily family) {
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        // prevent duplicate family record
        familyRepository.findByProfile(profile).ifPresent(f -> {
            throw new RuntimeException("Family details already exist. Use PUT to update.");
        });

        family.setProfile(profile);
        return familyRepository.save(family);
    }

    // ===== PUT (UPDATE) =====
    // ===== PUT (UPDATE) =====
    public ProfileFamily update(String email, ProfileFamily family) {

        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        ProfileFamily existing = familyRepository.findByProfile(profile)
                .orElse(null);

        // If no record exists, create new
        if (existing == null) {
            family.setProfile(profile);
            return familyRepository.save(family);
        }

        // Otherwise update existing
        existing.setFatherName(family.getFatherName());
        existing.setFatherDob(family.getFatherDob());
        existing.setMotherName(family.getMotherName());
        existing.setMotherDob(family.getMotherDob());
        existing.setMaritalStatus(family.getMaritalStatus());

        return familyRepository.save(existing);
    }

    // ===== DELETE =====
    public void delete(String email) {
        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        ProfileFamily existing = familyRepository.findByProfile(profile)
                .orElseThrow(() -> new RuntimeException("Family details not found"));

        familyRepository.delete(existing);
    }
}