package com.example.employeebackend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.employeebackend.entity.ProfileBank;
import com.example.employeebackend.service.ProfileBankService;

@RestController
@RequestMapping("/api/profile/bank")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfileBankController {

    private final ProfileBankService service;

    public ProfileBankController(ProfileBankService service) {
        this.service = service;
    }

    // ===== GET =====
    @GetMapping("/{email}")
    public ResponseEntity<ProfileBank> get(@PathVariable String email) {
        return ResponseEntity.ok(service.getByEmail(email));
    }

    // ===== POST =====
    @PostMapping("/{email}")
    public ResponseEntity<ProfileBank> create(
            @PathVariable String email,
            @RequestBody ProfileBank bank) {
        return ResponseEntity.ok(service.create(email, bank));
    }

    // ===== PUT =====
    @PutMapping("/{email}")
    public ResponseEntity<?> update(
            @PathVariable String email,
            @RequestBody ProfileBank bank) {
        service.update(email, bank);
        return ResponseEntity.ok(Map.of("message", "Bank details updated"));
    }

    // ===== DELETE =====
    @DeleteMapping("/{email}")
    public ResponseEntity<?> delete(@PathVariable String email) {
        service.delete(email);
        return ResponseEntity.ok(Map.of("message", "Bank details deleted"));
    }
}