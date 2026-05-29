package com.example.employeebackend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.employeebackend.entity.Profile;
import com.example.employeebackend.entity.ProfileProfessional;
import com.example.employeebackend.repository.ProfileProfessionalRepository;

@Service
@Transactional
public class ProfileProfessionalService {

    private final ProfileService profileService;
    private final ProfileProfessionalRepository repo;

    public ProfileProfessionalService(
            ProfileService profileService,
            ProfileProfessionalRepository repo
    ) {
        this.profileService = profileService;
        this.repo = repo;
    }

    // ===== GET ALL EXPERIENCES BY EMAIL =====
    public List<ProfileProfessional> getByEmail(String email) {
        Profile profile = profileService.getProfile(email);
        return repo.findByProfile(profile);
    }

    // ===== ADD EXPERIENCE =====
    public ProfileProfessional add(String email, ProfileProfessional professional) {
        Profile profile = profileService.getProfile(email);
        professional.setProfile(profile);
        return repo.save(professional);
    }

    // ===== UPDATE EXPERIENCE =====
    public ProfileProfessional update(Long id, ProfileProfessional data) {

        ProfileProfessional existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Experience not found"));

        if (data.getCompanyName() != null)
            existing.setCompanyName(data.getCompanyName());

        if (data.getDesignation() != null)
            existing.setDesignation(data.getDesignation());

        existing.setFresher(data.getFresher());
        existing.setFromDate(data.getFromDate());
        existing.setToDate(data.getToDate());
        existing.setCurrentlyWorking(data.getCurrentlyWorking());

        return repo.save(existing);
    }

    // ===== DELETE EXPERIENCE =====
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Experience not found");
        }
        repo.deleteById(id);
    }
}