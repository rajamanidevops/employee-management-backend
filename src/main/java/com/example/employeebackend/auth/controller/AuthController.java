/*package com.example.employeebackend.auth.controller;

import com.example.employeebackend.auth.dto.*;
import com.example.employeebackend.auth.service.AuthService;
import com.example.employeebackend.entity.Employee;
import com.example.employeebackend.entity.User;
import com.example.employeebackend.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmployeeService employeeService;

    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) {
        String result = authService.register(request);

        if ("EMAIL_EXISTS".equals(result)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(false, "Email already exists"));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(true, "Register successful"));
    }

    // ================= LOGIN =================
    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        User user = authService.login(request);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "User not found"));
        }

        if (user.getId() == null) { // empty user returned for invalid password
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Invalid password"));
        }

        // ✅ Fetch role from Employee table first
        Employee emp = employeeService.getByEmail(user.getEmail())
                .stream()
                .findFirst()
                .orElse(null);

        String roleName = (emp != null && emp.getRole() != null) ? emp.getRole().getRole()
                : (user.getRole() != null ? user.getRole().getRole() : null);

        if (roleName == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse(false, "User has no role assigned"));
        }

        return ResponseEntity.ok(
                new ApiResponse(true, "Login successful", null, roleName)
        );
    }

    // ================= FORGOT PASSWORD =================
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String result = authService.forgotPassword(request);

        if ("USER_NOT_FOUND".equals(result)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "User not found"));
        }

        if ("PASSWORD_MISMATCH".equals(result)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse(false, "Passwords do not match"));
        }

        return ResponseEntity.ok(new ApiResponse(true, "Password updated successfully"));
    }
}*/
