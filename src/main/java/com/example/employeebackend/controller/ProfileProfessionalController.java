package com.example.employeebackend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.employeebackend.entity.ProfileProfessional;
import com.example.employeebackend.service.ProfileProfessionalService;

@RestController
@RequestMapping("/api/profile/professional")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfileProfessionalController {

    private final ProfileProfessionalService service;

    public ProfileProfessionalController(ProfileProfessionalService service) {
        this.service = service;
    }

    // ===== GET =====
    @GetMapping("/{email}")
    public ResponseEntity<List<ProfileProfessional>> get(@PathVariable String email) {
        return ResponseEntity.ok(service.getByEmail(email));
    }

    // ===== POST =====
    @PostMapping("/{email}")
    public ResponseEntity<ProfileProfessional> add(
            @PathVariable String email,
            @RequestBody ProfileProfessional professional
    ) {
        return ResponseEntity.ok(service.add(email, professional));
    }

    // ===== PUT =====
    @PutMapping("/{id}")
    public ResponseEntity<ProfileProfessional> update(
            @PathVariable Long id,
            @RequestBody ProfileProfessional professional
    ) {
        return ResponseEntity.ok(service.update(id, professional));
    }

    // ===== DELETE =====
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(Map.of("message", "Experience deleted"));
    }
}