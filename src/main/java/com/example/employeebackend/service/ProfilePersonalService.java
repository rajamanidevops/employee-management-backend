package com.example.employeebackend.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.employeebackend.entity.Profile;
import com.example.employeebackend.entity.ProfilePersonal;
import com.example.employeebackend.repository.ProfilePersonalRepository;

@Service
@Transactional
public class ProfilePersonalService {

    private final ProfileService profileService;
    private final ProfilePersonalRepository repo;

    public ProfilePersonalService(ProfileService profileService,
                                  ProfilePersonalRepository repo) {
        this.profileService = profileService;
        this.repo = repo;
    }

    // ===== GET OR CREATE =====
    public ProfilePersonal getByEmail(String email) {
        Profile profile = profileService.getProfile(email);

        return repo.findByProfile(profile)
                .orElseGet(() -> {
                    ProfilePersonal p = new ProfilePersonal();
                    p.setProfile(profile);
                    return repo.save(p);
                });
    }

    // ===== CREATE (POST) =====
    public ProfilePersonal create(String email, ProfilePersonal data) {
        Profile profile = profileService.getProfile(email);

        Optional<ProfilePersonal> existing = repo.findByProfile(profile);
        if (existing.isPresent()) {
            throw new RuntimeException("Personal details already exist");
        }

        data.setId(null);
        data.setProfile(profile);
        return repo.save(data);
    }

    // ===== UPDATE (PUT) =====
    public ProfilePersonal update(String email, ProfilePersonal data) {
        ProfilePersonal existing = getByEmail(email);

        data.setId(existing.getId());
        data.setProfile(existing.getProfile());

        return repo.save(data);
    }

    // ===== DELETE =====
    public void delete(String email) {
        Profile profile = profileService.getProfile(email);

        repo.findByProfile(profile).ifPresent(repo::delete);
    }
}