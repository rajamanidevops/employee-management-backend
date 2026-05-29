package com.example.employeebackend.service;

import org.springframework.stereotype.Service;

import com.example.employeebackend.entity.Profile;
import com.example.employeebackend.entity.ProfileBank;
import com.example.employeebackend.repository.ProfileBankRepository;
import com.example.employeebackend.repository.ProfileRepository;

@Service
public class ProfileBankService {

    private final ProfileBankRepository bankRepo;
    private final ProfileRepository profileRepo;

    public ProfileBankService(ProfileBankRepository bankRepo,
                              ProfileRepository profileRepo) {
        this.bankRepo = bankRepo;
        this.profileRepo = profileRepo;
    }

    // ===== GET =====
    // ===== GET =====
    public ProfileBank getByEmail(String email) {
        Profile profile = profileRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        return bankRepo.findByProfile(profile)
                .orElse(new ProfileBank());
    }

    // ===== POST (CREATE) =====
    public ProfileBank create(String email, ProfileBank bank) {
        Profile profile = profileRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        bankRepo.findByProfile(profile).ifPresent(b -> {
            throw new RuntimeException("Bank details already exist");
        });

        bank.setProfile(profile);
        return bankRepo.save(bank);
    }

    // ===== PUT (UPDATE) =====
    public void update(String email, ProfileBank bank) {

        Profile profile = profileRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        ProfileBank existing = bankRepo.findByProfile(profile).orElse(null);

        if (existing == null) {
            bank.setProfile(profile);
            bankRepo.save(bank);
            return;
        }

        existing.setBankName(bank.getBankName());
        existing.setAccountHolderName(bank.getAccountHolderName());
        existing.setAccountNumber(bank.getAccountNumber());
        existing.setIfscCode(bank.getIfscCode());
        existing.setSalaryPayMode(bank.getSalaryPayMode());
        existing.setReimbursementSameAsSalary(bank.getReimbursementSameAsSalary());
        existing.setReimbursementPayMode(bank.getReimbursementPayMode());

        bankRepo.save(existing);
    }

    // ===== DELETE =====
    public void delete(String email) {
        ProfileBank existing = getByEmail(email);
        bankRepo.delete(existing);
    }
}