package com.example.employeebackend.attendance.controller;

import com.example.employeebackend.attendance.dto.AdminDashboardCardsDto;
import com.example.employeebackend.attendance.service.AdminDashboardService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminDashboardController {

    private final AdminDashboardService service;

    public AdminDashboardController(AdminDashboardService service) {
        this.service = service;
    }

    // ✅ SINGLE API FOR ALL TOP CARDS
    @GetMapping("/cards")
    public AdminDashboardCardsDto dashboardCards() {
        return service.getDashboardCards();
    }
    @GetMapping("/donut")
    public AdminDashboardCardsDto.TodayAttendanceDonutDto getTodayDonut() {
        return service.getTodayDonut();
    }
}