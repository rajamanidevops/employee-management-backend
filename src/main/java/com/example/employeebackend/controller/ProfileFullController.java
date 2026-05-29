package com.example.employeebackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.employeebackend.dto.ProfileFullResponse;
import com.example.employeebackend.entity.*;
import com.example.employeebackend.service.*;

import java.util.List;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfileFullController {

    private final ProfileService profileService;
    private final ProfileSnapshotService snapshotService;
    private final ProfilePersonalService personalService;
    private final ProfileFamilyService familyService;
    private final ProfileBankService bankService;
    private final ProfileDocumentsService documentsService;
    private final ProfileEducationService educationService;
    private final ProfileProfessionalService professionalService;
    private final ProfileHistoryService historyService;

    public ProfileFullController(
            ProfileService profileService,
            ProfileSnapshotService snapshotService,
            ProfilePersonalService personalService,
            ProfileFamilyService familyService,
            ProfileBankService bankService,
            ProfileDocumentsService documentsService,
            ProfileEducationService educationService,
            ProfileProfessionalService professionalService,
            ProfileHistoryService historyService
    ) {
        this.profileService = profileService;
        this.snapshotService = snapshotService;
        this.personalService = personalService;
        this.familyService = familyService;
        this.bankService = bankService;
        this.documentsService = documentsService;
        this.educationService = educationService;
        this.professionalService = professionalService;
        this.historyService = historyService;
    }

    // ===== FULL PROFILE =====
    @GetMapping("/full/{email}")
    public ResponseEntity<ProfileFullResponse> getFullProfile(@PathVariable String email) {

        Profile profile = profileService.getProfile(email);
        if (profile == null) {
            return ResponseEntity.notFound().build();
        }

        ProfileFullResponse res = new ProfileFullResponse();

        res.setProfile(profile);
        res.setSnapshot(snapshotService.getOrCreate(email));

        // Ensure empty objects instead of null
        res.setPersonal(personalService.getByEmail(email) != null ? personalService.getByEmail(email) : new ProfilePersonal());
        res.setFamily(familyService.getByEmail(email) != null ? familyService.getByEmail(email) : new ProfileFamily());
        res.setBank(bankService.getByEmail(email) != null ? bankService.getByEmail(email) : new ProfileBank());
        res.setDocuments(documentsService.getByEmail(email) != null ? documentsService.getByEmail(email) : new ProfileDocuments());

        res.setEducation(educationService.getByEmail(email) != null ? educationService.getByEmail(email) : List.of());
        res.setProfessional(professionalService.getByEmail(email) != null ? professionalService.getByEmail(email) : List.of());
        res.setHistory(historyService.getHistory(email) != null ? historyService.getHistory(email) : List.of());

        return ResponseEntity.ok(res);
    }
}