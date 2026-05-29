package com.example.employeebackend.attendance.controller;

import com.example.employeebackend.attendance.dto.AttendanceOverrideRequest;
import com.example.employeebackend.attendance.dto.AttendanceResponse;
import com.example.employeebackend.attendance.entity.Attendance;
import com.example.employeebackend.attendance.entity.AttendanceStatus;
import com.example.employeebackend.attendance.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/manager/attendance")
@CrossOrigin(origins = "http://localhost:4200")
public class ManagerAttendanceController {

    private final AttendanceService attendanceService;

    public ManagerAttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // =========================
    // OVERRIDE ATTENDANCE
    // =========================
    @PostMapping("/override")
    public ResponseEntity<?> overrideAttendance(
            @RequestBody AttendanceOverrideRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {

        String managerEmail = jwt.getClaimAsString("email");

        if (request.getEmployeeEmail() == null || request.getDate() == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Missing required fields"));
        }

        Attendance attendance = attendanceService.overrideAttendance(
                managerEmail,
                request.getEmployeeEmail(),
                request.getDate(),
                AttendanceStatus.valueOf(request.getStatus().toUpperCase()),
                request.getWorkingMinutes()
        );

        return ResponseEntity.ok(
                Map.of(
                        "message", "Attendance overridden successfully",
                        "data", attendance
                )
        );
    }

    // =========================
    // TEAM ATTENDANCE (DATE / RANGE)
    // =========================
    @GetMapping("/team")
    public ResponseEntity<List<AttendanceResponse>> teamAttendance(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @AuthenticationPrincipal Jwt jwt
    ) {

        String managerEmail = jwt.getClaimAsString("email");

        // SINGLE DATE
        if (date != null) {
            LocalDate d = LocalDate.parse(date);

            return ResponseEntity.ok(
                    attendanceService.getTeamAttendance(managerEmail, d, d)
            );
        }

        // DATE RANGE
        if (from != null && to != null) {
            LocalDate fromDate = LocalDate.parse(from);
            LocalDate toDate = LocalDate.parse(to);

            return ResponseEntity.ok(
                    attendanceService.getTeamAttendance(managerEmail, fromDate, toDate)
            );
        }

        // DEFAULT (TODAY OR FULL RANGE LOGIC CAN BE ADDED)
        return ResponseEntity.ok(
                attendanceService.getTeamAttendance(managerEmail, LocalDate.now(), LocalDate.now())
        );
    }

    // =========================
    // OPTIONAL: DEBUG ENDPOINT
    // =========================
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Manager Attendance API Working");
    }
}