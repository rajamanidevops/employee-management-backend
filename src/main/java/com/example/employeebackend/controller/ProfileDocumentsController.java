package com.example.employeebackend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.employeebackend.entity.ProfileDocuments;
import com.example.employeebackend.service.ProfileDocumentsService;

@RestController
@RequestMapping("/api/profile/documents")
@CrossOrigin(origins = "http://localhost:4200")
public class ProfileDocumentsController {

    private final ProfileDocumentsService service;

    public ProfileDocumentsController(ProfileDocumentsService service) {
        this.service = service;
    }

    // ===== GET =====
    @GetMapping("/{email}")
    public ResponseEntity<ProfileDocuments> get(@PathVariable String email) {
        return ResponseEntity.ok(service.getByEmail(email));
    }

    // ===== POST =====
    @PostMapping("/{email}")
    public ResponseEntity<ProfileDocuments> create(
            @PathVariable String email,
            @RequestBody ProfileDocuments documents) {
        return ResponseEntity.ok(service.create(email, documents));
    }

    // ===== PUT =====
    @PutMapping("/{email}")
    public ResponseEntity<?> update(
            @PathVariable String email,
            @RequestBody ProfileDocuments documents) {
        service.update(email, documents);
        return ResponseEntity.ok(Map.of("message", "Documents updated"));
    }

    // ===== DELETE =====
    @DeleteMapping("/{email}")
    public ResponseEntity<?> delete(@PathVariable String email) {
        service.delete(email);
        return ResponseEntity.ok(Map.of("message", "Documents deleted"));
    }
}