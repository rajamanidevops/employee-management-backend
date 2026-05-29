package com.example.employeebackend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.employeebackend.entity.ProfileEducation;
import com.example.employeebackend.service.ProfileEducationService;

@RestController
@RequestMapping("/api/profile/education")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfileEducationController {

    private final ProfileEducationService service;

    public ProfileEducationController(ProfileEducationService service) {
        this.service = service;
    }

    // ===== GET ALL =====
    @GetMapping("/{email}")
    public ResponseEntity<List<ProfileEducation>> get(@PathVariable String email) {
        return ResponseEntity.ok(service.getByEmail(email));
    }

    // ===== POST =====
    @PostMapping("/{email}")
    public ResponseEntity<ProfileEducation> add(
            @PathVariable String email,
            @RequestBody ProfileEducation education) {
        return ResponseEntity.ok(service.add(email, education));
    }

    // ===== PUT =====
    @PutMapping("/{id}")
    public ResponseEntity<ProfileEducation> update(
            @PathVariable Long id,
            @RequestBody ProfileEducation education) {
        return ResponseEntity.ok(service.update(id, education));
    }

    // ===== DELETE =====
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(Map.of("message", "Education deleted"));
    }
}