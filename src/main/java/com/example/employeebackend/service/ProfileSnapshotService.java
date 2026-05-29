package com.example.employeebackend.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.employeebackend.entity.Profile;
import com.example.employeebackend.entity.ProfileSnapshot;
import com.example.employeebackend.repository.ProfileSnapshotRepository;

@Service
@Transactional
public class ProfileSnapshotService {

    private final ProfileService profileService;
    private final ProfileSnapshotRepository snapshotRepo;

    public ProfileSnapshotService(ProfileService profileService, ProfileSnapshotRepository snapshotRepo) {
        this.profileService = profileService;
        this.snapshotRepo = snapshotRepo;
    }

    /**
     * Get existing snapshot or create a new one if it doesn't exist.
     */
    public ProfileSnapshot getOrCreate(String email) {
        // ✅ Use getProfile to fetch existing profile, no empty profile creation
        Profile profile = profileService.getProfile(email);

        return snapshotRepo.findByProfile(profile)
                .orElseGet(() -> {
                    ProfileSnapshot snapshot = new ProfileSnapshot();
                    snapshot.setProfile(profile);
                    return snapshotRepo.save(snapshot);
                });
    }

    /**
     * Create a new snapshot for the profile by email.
     * Throws exception if snapshot already exists.
     */
    public ProfileSnapshot create(String email, ProfileSnapshot snapshotData) {
        Profile profile = profileService.getProfile(email);

        Optional<ProfileSnapshot> existing = snapshotRepo.findByProfile(profile);
        if (existing.isPresent()) {
            throw new RuntimeException("Snapshot already exists for this profile");
        }

        snapshotData.setProfile(profile);
        return snapshotRepo.save(snapshotData);
    }

    /**
     * Update an existing snapshot for the profile by email.
     * Only replaces existing snapshot with new data.
     */
    public ProfileSnapshot update(String email, ProfileSnapshot data) {
        ProfileSnapshot existing = getOrCreate(email);

        // Keep the same ID and profile reference
        data.setId(existing.getId());
        data.setProfile(existing.getProfile());

        return snapshotRepo.save(data);
    }
}