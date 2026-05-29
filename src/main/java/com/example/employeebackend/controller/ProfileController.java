package com.example.employeebackend.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.employeebackend.entity.Profile;
import com.example.employeebackend.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfileController {

    private final ProfileService service;

    public ProfileController(ProfileService service) {
        this.service = service;
    }

    // ===== CREATE NEW PROFILE =====
    // ===== CREATE PROFILE =====
    @PostMapping
    public ResponseEntity<Map<String, Object>> createProfile(@RequestBody Profile profile) {

        if (profile.getEmail() == null || profile.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email is required"));
        }

        // Check if profile already exists
        Profile existing = service.getOrCreateProfile(profile.getEmail());
        if (existing.getName() != null) { // already has details
            return ResponseEntity.badRequest().body(Map.of("error", "Profile already exists"));
        }

        // Save profile details
        service.updateDetails(profile.getEmail(), profile);

        return ResponseEntity.ok(Map.of(
                "message", "Profile created successfully",
                "email", profile.getEmail()
        ));
    }

    // ===== GET PROFILE =====
    @GetMapping("/{email}")
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable String email) {
        Profile p = service.getOrCreateProfile(email);// changed from getOrCreateProfile to getProfile

        Map<String, Object> res = new HashMap<>();
        res.put("email", p.getEmail());
        res.put("name", p.getName());
        res.put("phone", p.getPhone());
        res.put("address", p.getAddress());
        res.put("designation", p.getDesignation());
        res.put(
                "image",
                p.getProfileImage() != null
                        ? Base64.getEncoder().encodeToString(p.getProfileImage())
                        : null
        );

        return ResponseEntity.ok(res);
    }

    // ===== UPDATE PROFILE DETAILS =====
    @PutMapping("/{email}")
    public ResponseEntity<Map<String, String>> updateProfile(
            @PathVariable String email,
            @RequestBody Profile profile
    ) {
        service.updateDetails(email, profile);
        return ResponseEntity.ok(Map.of("message", "Profile updated successfully"));
    }

    // ===== UPLOAD PROFILE IMAGE =====
    @PostMapping("/{email}/image")
    public ResponseEntity<Map<String, String>> uploadImage(
            @PathVariable String email,
            @RequestParam("image") MultipartFile image // change key to 'image'
    ) {
        service.uploadImage(email, image);
        return ResponseEntity.ok(Map.of("message", "Profile image uploaded"));
    }
    // ===== DELETE PROFILE IMAGE =====
    @DeleteMapping("/{email}/image")
    public ResponseEntity<Map<String, String>> deleteImage(@PathVariable String email) {
        service.deleteImage(email);
        return ResponseEntity.ok(Map.of("message", "Profile image deleted"));
    }
}