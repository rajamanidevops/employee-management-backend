/*package com.example.employeebackend.controller;

import com.example.employeebackend.dto.ManagerTeamResponse;
import com.example.employeebackend.service.ManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    // =========================
    // MANAGER: VIEW OWN TEAM
    // =========================
    @GetMapping("/manager/team")
    public ManagerTeamResponse getMyTeam(
            @AuthenticationPrincipal Jwt jwt
    ) {
        String email = jwt.getClaimAsString("email");
        return managerService.getMyTeam(email);
    }

    // =========================
    // CHECK IF USER IS MANAGER
    // =========================
    @GetMapping("/manager/is-manager")
    public Map<String, Boolean> isManager(
            @AuthenticationPrincipal Jwt jwt
    ) {
        String email = jwt.getClaimAsString("email");
        boolean isManager = managerService.isManager(email);
        return Map.of("isManager", isManager);
    }

    // =========================
    // ADMIN: ASSIGN MANAGER
    // =========================
    @PutMapping("/admin/employees/{empNo}/assign-manager/{managerEmpNo}")
    public ResponseEntity<?> assignManager(
            @PathVariable String empNo,
            @PathVariable String managerEmpNo,
            @AuthenticationPrincipal Jwt jwt
    ) {

        if (!hasAdminRole(jwt)) {
            return ResponseEntity.status(403).body("Access denied");
        }

        managerService.assignManager(empNo, managerEmpNo);
        return ResponseEntity.ok(Map.of("message", "Manager assigned successfully"));
    }

    // =========================
    // ADMIN: REMOVE MANAGER
    // =========================
    @PutMapping("/admin/employees/{empNo}/remove-manager")
    public ResponseEntity<?> removeManager(
            @PathVariable String empNo,
            @AuthenticationPrincipal Jwt jwt
    ) {

        if (!hasAdminRole(jwt)) {
            return ResponseEntity.status(403).body("Access denied");
        }

        managerService.removeManager(empNo);
        return ResponseEntity.ok(Map.of("message", "Manager removed successfully"));
    }

    // =========================
    // ADMIN: VIEW MANAGER TEAM
    // =========================
    @GetMapping("/admin/manager/{managerEmpNo}/team")
    public ManagerTeamResponse getManagerTeam(
            @PathVariable String managerEmpNo,
            @AuthenticationPrincipal Jwt jwt
    ) {

        if (!hasAdminRole(jwt)) {
            throw new RuntimeException("Access denied");
        }

        return managerService.getManagerTeam(managerEmpNo);
    }

    // =========================
    // ROLE CHECK (SAFE)
    // =========================
    private boolean hasAdminRole(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null) return false;

        List<String> roles = (List<String>) realmAccess.get("roles");
        return roles != null && roles.contains("ADMIN");
    }
}*/