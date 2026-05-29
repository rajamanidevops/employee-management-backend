package com.example.employeebackend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.employeebackend.entity.ProfilePersonal;
import com.example.employeebackend.service.ProfilePersonalService;

@RestController
@RequestMapping("/api/profile/personal")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfilePersonalController {

    private final ProfilePersonalService service;

    public ProfilePersonalController(ProfilePersonalService service) {
        this.service = service;
    }

    // ===== GET =====
    @GetMapping("/{email}")
    public ResponseEntity<ProfilePersonal> get(@PathVariable String email) {
        return ResponseEntity.ok(service.getByEmail(email));
    }

    // ===== POST (CREATE) =====
    @PostMapping("/{email}")
    public ResponseEntity<ProfilePersonal> create(
            @PathVariable String email,
            @RequestBody ProfilePersonal personal
    ) {
        return ResponseEntity.ok(service.create(email, personal));
    }

    // ===== PUT (UPDATE) =====
    @PutMapping("/{email}")
    public ResponseEntity<?> update(
            @PathVariable String email,
            @RequestBody ProfilePersonal personal
    ) {
        service.update(email, personal);
        return ResponseEntity.ok(Map.of("message", "Personal details updated"));
    }

    // ===== DELETE =====
    @DeleteMapping("/{email}")
    public ResponseEntity<?> delete(@PathVariable String email) {
        service.delete(email);
        return ResponseEntity.ok(Map.of("message", "Personal details deleted"));
    }
}