/*package com.example.employeebackend.dashboard.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.employeebackend.dashboard.dto.DashboardDTO;
import com.example.employeebackend.dashboard.service.DashboardService;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // ✅ MAIN DASHBOARD STATS
    @GetMapping
    public DashboardDTO getDashboard() {
        return dashboardService.getDashboardData();
    }

    // ✅ PROFILE COMPLETION STATUS (TAB WISE)
    @GetMapping("/profile-completion")
    public Map<String, Boolean> getProfileCompletion(@RequestParam String email) {
        return dashboardService.getProfileCompletion(email);
    }
}*/
package com.example.employeebackend.dashboard.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.employeebackend.dashboard.dto.DashboardDTO;
import com.example.employeebackend.dashboard.service.DashboardService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // 🔐 Any authenticated user
    @GetMapping
    public DashboardDTO getDashboard() {
        return dashboardService.getDashboardData();
    }

    // 🔐 Logged-in user's profile only
    @GetMapping("/profile-completion")
    public Map<String, Boolean> getProfileCompletion(
            @AuthenticationPrincipal Jwt jwt) {

        String email = jwt.getClaimAsString("email");
        return dashboardService.getProfileCompletion(email);
    }
}