package com.example.employeebackend.attendance.controller;

import com.example.employeebackend.attendance.dto.LeaveActionRequest;
import com.example.employeebackend.attendance.entity.Leave;
import com.example.employeebackend.attendance.service.LeaveService;
import com.example.employeebackend.entity.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import com.example.employeebackend.repository.EmployeeRepository;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/manager/leaves")
@CrossOrigin(origins = "http://localhost:4200")
public class ManagerLeaveController {

    private final LeaveService leaveService;
    private final EmployeeRepository employeeRepo;
    public ManagerLeaveController(
            LeaveService leaveService,
            EmployeeRepository employeeRepo
    ) {
        this.leaveService = leaveService;
        this.employeeRepo = employeeRepo;
    }

    // =========================
    // VIEW PENDING LEAVES
    // =========================
    @GetMapping("/pending")
    public ResponseEntity<List<Leave>> getPendingLeaves(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(
                leaveService.getPendingLeavesForManager(
                        jwt.getClaimAsString("email")
                )
        );
    }

    // =========================
    // APPROVE LEAVE
    // =========================
    @PutMapping("/{leaveId}/approve")
    public ResponseEntity<?> approveLeave(
            @PathVariable Long leaveId,
            @AuthenticationPrincipal Jwt jwt
    ) {

        leaveService.updateLeaveStatus(
                leaveId,
                "APPROVED",
                jwt.getClaimAsString("email"),
                null
        );

        return ResponseEntity.ok(Map.of("message", "Leave approved"));
    }

    // =========================
    // REJECT LEAVE
    // =========================
    @PutMapping("/{leaveId}/reject")
    public ResponseEntity<?> rejectLeave(
            @PathVariable Long leaveId,
            @RequestBody LeaveActionRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {

        leaveService.updateLeaveStatus(
                leaveId,
                "REJECTED",
                jwt.getClaimAsString("email"),
                request.getRemarks()
        );

        return ResponseEntity.ok(Map.of("message", "Leave rejected"));
    }

    @GetMapping("/is-manager")
    public ResponseEntity<Boolean> isManager(@AuthenticationPrincipal Jwt jwt) {

        String email = jwt.getClaimAsString("email");

        Employee user = employeeRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isManager = employeeRepo.existsByManager(user);

        return ResponseEntity.ok(isManager);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Leave>> getAllLeaves(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(
                leaveService.getAllLeavesForManager(
                        jwt.getClaimAsString("email")
                )
        );
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Long>> getSummary(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(
                leaveService.getManagerSummary(
                        jwt.getClaimAsString("email")
                )
        );
    }
}