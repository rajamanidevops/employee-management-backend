package com.example.employeebackend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.employeebackend.entity.ProfileHistory;
import com.example.employeebackend.service.ProfileHistoryService;

@RestController
@RequestMapping("/api/profile/history")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfileHistoryController {

    private final ProfileHistoryService service;

    public ProfileHistoryController(ProfileHistoryService service) {
        this.service = service;
    }

    // ===== GET =====
    @GetMapping("/{email}")
    public ResponseEntity<List<ProfileHistory>> getHistory(@PathVariable String email) {
        return ResponseEntity.ok(service.getHistory(email));
    }

    // ===== POST =====
    @PostMapping("/{email}")
    public ResponseEntity<?> addHistory(
            @PathVariable String email,
            @RequestBody ProfileHistory history
    ) {
        service.addHistory(email, history);
        return ResponseEntity.ok(Map.of("message", "History added"));
    }

    // ===== PUT =====
    @PutMapping("/{id}")
    public ResponseEntity<ProfileHistory> updateHistory(
            @PathVariable Long id,
            @RequestBody ProfileHistory history
    ) {
        return ResponseEntity.ok(service.updateHistory(id, history));
    }

    // ===== DELETE =====
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHistory(@PathVariable Long id) {
        service.deleteHistory(id);
        return ResponseEntity.ok(Map.of("message", "History deleted"));
    }
}