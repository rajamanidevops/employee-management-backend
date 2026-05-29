package com.example.employeebackend.attendance.controller;

import java.io.PrintWriter;

import com.example.employeebackend.attendance.dto.AdminAttendanceUpdateDto;
import com.example.employeebackend.attendance.entity.Attendance;
import com.example.employeebackend.attendance.service.AdminAttendanceService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
@RestController
@RequestMapping("/api/admin/attendance")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminAttendanceController {

    private final AdminAttendanceService service;

    public AdminAttendanceController(AdminAttendanceService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public Page<Attendance> getAttendanceList(
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.getAttendanceList(fromDate, toDate, department, page, size);
    }

    @GetMapping("/monthly")
    public List<Attendance> getMonthlyAttendance(
            @RequestParam String email,
            @RequestParam int month,
            @RequestParam int year
    ) {
        return service.getMonthlyAttendance(email, month, year);
    }

    @GetMapping("/monthly/export")
    public void exportMonthlyAttendance(
            @RequestParam String email,
            @RequestParam int month,
            @RequestParam int year,
            HttpServletResponse response
    ) throws IOException {

        response.setContentType("text/csv");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=attendance_" + month + "_" + year + ".csv"
        );

        List<Attendance> list =
                service.getMonthlyAttendance(email, month, year);

        PrintWriter writer = response.getWriter();
        writer.println("Date,Status,CheckIn,CheckOut,WorkingMinutes");

        for (Attendance a : list) {
            writer.println(
                    a.getAttendanceDate() + "," +
                            a.getStatus() + "," +
                            a.getCheckInTime() + "," +
                            a.getCheckOutTime() + "," +
                            a.getWorkingMinutes()
            );
        }
        writer.flush();
    }
    @PutMapping("/{id}")
    public Attendance updateAttendance(
            @PathVariable Long id,
            @RequestBody AdminAttendanceUpdateDto dto
    ) {
        return service.updateAttendance(id, dto);
    }
}