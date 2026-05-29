package com.example.employeebackend.service;

import org.springframework.stereotype.Service;

import com.example.employeebackend.entity.Profile;
import com.example.employeebackend.entity.ProfileDocuments;
import com.example.employeebackend.repository.ProfileDocumentsRepository;
import com.example.employeebackend.repository.ProfileRepository;

@Service
public class ProfileDocumentsService {

    private final ProfileDocumentsRepository documentsRepo;
    private final ProfileRepository profileRepo;

    public ProfileDocumentsService(ProfileDocumentsRepository documentsRepo,
                                   ProfileRepository profileRepo) {
        this.documentsRepo = documentsRepo;
        this.profileRepo = profileRepo;
    }

    // ===== GET =====
    // ===== GET =====
    public ProfileDocuments getByEmail(String email) {
        Profile profile = profileRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return documentsRepo.findByProfile(profile)
                .orElse(new ProfileDocuments());
    }

    // ===== POST (CREATE) =====
    public ProfileDocuments create(String email, ProfileDocuments documents) {

        Profile profile = profileRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        ProfileDocuments existing = documentsRepo.findByProfile(profile)
                .orElse(null);

        // If already exists → update instead of error
        if (existing != null) {
            existing.setAadhaar(documents.getAadhaar());
            existing.setPan(documents.getPan());
            existing.setResume(documents.getResume());
            existing.setOfferLetter(documents.getOfferLetter());
            existing.setExperienceLetter(documents.getExperienceLetter());

            return documentsRepo.save(existing);
        }

        documents.setProfile(profile);
        return documentsRepo.save(documents);
    }

    // ===== PUT (UPDATE) =====
    public void update(String email, ProfileDocuments documents) {

        Profile profile = profileRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        ProfileDocuments existing = documentsRepo.findByProfile(profile)
                .orElse(null);

        // If documents don't exist yet → create
        if (existing == null) {
            documents.setProfile(profile);
            documentsRepo.save(documents);
            return;
        }

        // Otherwise update
        existing.setAadhaar(documents.getAadhaar());
        existing.setPan(documents.getPan());
        existing.setResume(documents.getResume());
        existing.setOfferLetter(documents.getOfferLetter());
        existing.setExperienceLetter(documents.getExperienceLetter());

        documentsRepo.save(existing);
    }

    // ===== DELETE =====
    public void delete(String email) {
        ProfileDocuments existing = getByEmail(email);
        documentsRepo.delete(existing);
    }
}