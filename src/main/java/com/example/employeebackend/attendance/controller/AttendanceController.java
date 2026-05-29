package com.example.employeebackend.attendance.controller;

import com.example.employeebackend.attendance.dto.AttendanceResponse;
import com.example.employeebackend.attendance.dto.LeaveRequest;
import com.example.employeebackend.attendance.entity.Attendance;
import com.example.employeebackend.attendance.entity.Leave;
import com.example.employeebackend.attendance.repository.LeaveRepository;
import com.example.employeebackend.attendance.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendance")
@CrossOrigin(origins = "http://localhost:4200")
public class AttendanceController {

    private final AttendanceService service;
    private final LeaveRepository leaveRepo;

    public AttendanceController(AttendanceService service,
                                LeaveRepository leaveRepo) {
        this.service = service;
        this.leaveRepo = leaveRepo;
    }

    /* ================= ATTENDANCE ================= */

    @PostMapping("/check-in/{email}")
    public Attendance checkIn(@PathVariable String email) {
        return service.checkIn(email);
    }

    @PostMapping("/check-out/{email}")
    public Attendance checkOut(@PathVariable String email) {
        return service.checkOut(email);
    }

    /* ================= DASHBOARD DATA ================= */

    @GetMapping("/present/{email}")
    public long present(@PathVariable String email) {
        return service.getPresentCount(email);
    }

    @GetMapping("/late/{email}")
    public long late(@PathVariable String email) {
        return service.getLateCount(email);
    }

    @GetMapping("/absent/{email}")
    public long absent(@PathVariable String email) {
        return service.getAbsentCount(email);
    }

    @GetMapping("/leave/{email}")
    public long leave(@PathVariable String email) {
        return service.getLeaveCount(email);
    }

    /* ================= LEAVE ================= */




    /* ================= MONTHLY ================= */

    @GetMapping("/monthly/{email}")
    public List<AttendanceResponse> monthly(@PathVariable String email) {
        return service.monthly(email);
    }
    @GetMapping("/calendar")
    public List<AttendanceResponse> calendar(
            @RequestParam String email,
            @RequestParam int year,
            @RequestParam int month
    ) {
        return service.getAutoMonthlyCalendar(email, year, month);
    }

}