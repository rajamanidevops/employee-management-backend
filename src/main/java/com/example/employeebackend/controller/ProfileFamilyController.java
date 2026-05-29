package com.example.employeebackend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.employeebackend.entity.ProfileFamily;
import com.example.employeebackend.service.ProfileFamilyService;

@RestController
@RequestMapping("/api/profile/family")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfileFamilyController {

    private final ProfileFamilyService service;

    public ProfileFamilyController(ProfileFamilyService service) {
        this.service = service;
    }

    // ===== GET =====
    @GetMapping("/{email}")
    public ResponseEntity<ProfileFamily> get(@PathVariable String email) {
        return ResponseEntity.ok(service.getByEmail(email));
    }

    // ===== POST =====
    @PostMapping("/{email}")
    public ResponseEntity<ProfileFamily> create(
            @PathVariable String email,
            @RequestBody ProfileFamily family) {
        return ResponseEntity.ok(service.create(email, family));
    }

    // ===== PUT =====
    @PutMapping("/{email}")
    public ResponseEntity<?> update(
            @PathVariable String email,
            @RequestBody ProfileFamily family) {
        service.update(email, family);
        return ResponseEntity.ok(Map.of("message", "Family details updated"));
    }

    // ===== DELETE =====
    @DeleteMapping("/{email}")
    public ResponseEntity<?> delete(@PathVariable String email) {
        service.delete(email);
        return ResponseEntity.ok(Map.of("message", "Family details deleted"));
    }
}