package com.example.employeebackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.employeebackend.entity.ProfileSnapshot;
import com.example.employeebackend.service.ProfileSnapshotService;

@RestController
@RequestMapping("/api/profile/snapshot")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfileSnapshotController {

    private final ProfileSnapshotService service;

    public ProfileSnapshotController(ProfileSnapshotService service) {
        this.service = service;
    }

    // ===== GET snapshot by email =====
    @GetMapping("/{email}")
    public ResponseEntity<ProfileSnapshot> get(@PathVariable String email) {
        ProfileSnapshot snapshot = service.getOrCreate(email);
        return ResponseEntity.ok(snapshot);
    }

    // ===== POST snapshot by email =====
    @PostMapping("/{email}")
    public ResponseEntity<?> create(@PathVariable String email,
                                    @RequestBody ProfileSnapshot snapshot) {
        try {
            ProfileSnapshot created = service.create(email, snapshot);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ===== PUT snapshot by email =====
    @PutMapping("/{email}")
    public ResponseEntity<?> update(@PathVariable String email,
                                    @RequestBody ProfileSnapshot snapshot) {
        ProfileSnapshot updated = service.update(email, snapshot);
        return ResponseEntity.ok(updated);
    }
}