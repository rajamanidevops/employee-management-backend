package com.example.employeebackend.attendance.controller;

import com.example.employeebackend.attendance.entity.Leave;
import com.example.employeebackend.attendance.entity.LeaveBalance;
import com.example.employeebackend.attendance.repository.LeaveBalanceRepository;
import com.example.employeebackend.attendance.service.LeaveService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee/leaves")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeLeaveController {

    private final LeaveService leaveService;
    private final LeaveBalanceRepository leaveBalanceRepo; // ✅ ADD

    // ✅ UPDATE CONSTRUCTOR
    public EmployeeLeaveController(
            LeaveService leaveService,
            LeaveBalanceRepository leaveBalanceRepo
    ) {
        this.leaveService = leaveService;
        this.leaveBalanceRepo = leaveBalanceRepo;
    }

    // =========================
    // APPLY LEAVE
    // =========================
    @PostMapping
    public ResponseEntity<Leave> applyLeave(
            @RequestBody Leave leave,
            @AuthenticationPrincipal Jwt jwt
    ) {

        String email = jwt.getClaimAsString("email");
        leave.setEmail(email);

        return ResponseEntity.ok(
                leaveService.applyLeave(leave)
        );
    }

    // =========================
    // LEAVE HISTORY
    // =========================
    @GetMapping
    public ResponseEntity<List<Leave>> getMyLeaves(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return ResponseEntity.ok(
                leaveService.getEmployeeLeaves(
                        jwt.getClaimAsString("email")
                )
        );
    }

    // =========================
    // LEAVE BALANCE
    // =========================
    @GetMapping("/balance")
    public LeaveBalance getBalance(
            @AuthenticationPrincipal Jwt jwt
    ) {
        return leaveBalanceRepo
                .findByEmail(jwt.getClaimAsString("email"))
                .orElseThrow(() ->
                        new RuntimeException("Leave balance not found")
                );
    }
    @GetMapping("/balance/{email}")
    public LeaveBalance getBalanceByEmail(@PathVariable String email) {
        return leaveBalanceRepo.findByEmail(email)
                .orElseGet(() -> {
                    LeaveBalance lb = new LeaveBalance();
                    lb.setEmail(email);
                    lb.setClBalance(12);
                    lb.setSlBalance(8);
                    lb.setElBalance(15);
                    leaveBalanceRepo.save(lb);
                    return lb;
                });
    }
}