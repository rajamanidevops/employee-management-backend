package com.example.employeebackend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.employeebackend.entity.Profile;
import com.example.employeebackend.entity.ProfileEducation;
import com.example.employeebackend.repository.ProfileEducationRepository;
import com.example.employeebackend.repository.ProfileRepository;

@Service
public class ProfileEducationService {

    private final ProfileEducationRepository educationRepo;
    private final ProfileRepository profileRepo;

    public ProfileEducationService(ProfileEducationRepository educationRepo,
                                   ProfileRepository profileRepo) {
        this.educationRepo = educationRepo;
        this.profileRepo = profileRepo;
    }

    // ===== GET ALL =====
    public List<ProfileEducation> getByEmail(String email) {
        Profile profile = profileRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return educationRepo.findByProfile(profile);
    }

    // ===== POST =====
    public ProfileEducation add(String email, ProfileEducation education) {
        Profile profile = profileRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        education.setProfile(profile);
        return educationRepo.save(education);
    }

    // ===== PUT =====
    public ProfileEducation update(Long id, ProfileEducation education) {
        ProfileEducation existing = educationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Education not found"));

        existing.setQualification(education.getQualification());
        existing.setDegree(education.getDegree());
        existing.setSpecialization(education.getSpecialization());
        existing.setInstitute(education.getInstitute());
        existing.setUniversity(education.getUniversity());
        existing.setYearOfPassing(education.getYearOfPassing());
        existing.setPercentageOrCgpa(education.getPercentageOrCgpa());
        existing.setHighestQualification(education.getHighestQualification());

        return educationRepo.save(existing);
    }

    // ===== DELETE =====
    public void delete(Long id) {
        if (!educationRepo.existsById(id)) {
            throw new RuntimeException("Education not found");
        }
        educationRepo.deleteById(id);
    }
}