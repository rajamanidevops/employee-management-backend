package com.example.employeebackend.attendance.controller;

import com.example.employeebackend.attendance.entity.ShiftConfig;
import com.example.employeebackend.attendance.repository.ShiftConfigRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/shift")
@PreAuthorize("hasRole('ADMIN')")
public class ShiftConfigController {

    private final ShiftConfigRepository repo;

    public ShiftConfigController(ShiftConfigRepository repo) {
        this.repo = repo;
    }

    // ================= CREATE / UPDATE SHIFT =================
    @PostMapping
    public ResponseEntity<ShiftConfig> save(@RequestBody ShiftConfig shift) {

        // Only ONE shift config allowed
        repo.deleteAll();

        ShiftConfig savedShift = repo.save(shift);
        return ResponseEntity.ok(savedShift);
    }

    // ================= GET SHIFT =================
    @GetMapping
    public ResponseEntity<ShiftConfig> get() {

        ShiftConfig shift = repo.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Shift not set"));

        return ResponseEntity.ok(shift);
    }
}
