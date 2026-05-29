package com.example.employeebackend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.employeebackend.entity.Profile;
import com.example.employeebackend.entity.ProfileHistory;
import com.example.employeebackend.repository.ProfileHistoryRepository;

@Service
@Transactional
public class ProfileHistoryService {

    private final ProfileService profileService;
    private final ProfileHistoryRepository repo;

    public ProfileHistoryService(
            ProfileService profileService,
            ProfileHistoryRepository repo
    ) {
        this.profileService = profileService;
        this.repo = repo;
    }

    // ===== GET HISTORY BY EMAIL =====
    public List<ProfileHistory> getHistory(String email) {
        Profile profile = profileService.getProfile(email);
        return repo.findByProfile(profile);
    }

    // ===== ADD HISTORY =====
    public ProfileHistory addHistory(String email, ProfileHistory history) {
        Profile profile = profileService.getProfile(email);
        history.setProfile(profile);
        return repo.save(history);
    }

    // ===== UPDATE HISTORY =====
    public ProfileHistory updateHistory(Long id, ProfileHistory data) {
        ProfileHistory existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("History not found"));

        existing.setType(data.getType());
        existing.setOldValue(data.getOldValue());
        existing.setNewValue(data.getNewValue());
        existing.setRequestedBy(data.getRequestedBy());
        existing.setApprovedBy(data.getApprovedBy());

        return repo.save(existing);
    }

    // ===== DELETE HISTORY =====
    public void deleteHistory(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("History not found");
        }
        repo.deleteById(id);
    }
}