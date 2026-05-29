/*package com.example.employeebackend.auth.service;

import com.example.employeebackend.auth.dto.*;
import com.example.employeebackend.entity.Employee;
import com.example.employeebackend.entity.User;
import com.example.employeebackend.entity.Role;
import com.example.employeebackend.repository.EmployeeRepository;
import com.example.employeebackend.repository.UserRepository;
import com.example.employeebackend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRepository roleRepository;

    // ================= REGISTER =================
    public String register(RegisterRequest request) {
        if (userRepository.findByEmailIgnoreCase(request.getEmail()).isPresent()) {
            return "EMAIL_EXISTS";
        }

        // Assign role (default USER)
        String roleName = request.getRole() != null ? request.getRole() : "USER";
        Role role = roleRepository.findByRole(roleName)
                .orElseThrow(() -> new RuntimeException(roleName + " role not found"));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(role);

        userRepository.save(user);
        return "REGISTER_SUCCESS";
    }

    // ================= LOGIN =================
    public User login(LoginRequest request) {

        // 1️⃣ Check User table first
        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(request.getEmail());
        if (userOpt.isEmpty()) return null; // user not found
        User user = userOpt.get();

        // 2️⃣ Check password
        if (!user.getPassword().equals(request.getPassword())) {
            return new User(); // empty user = invalid password
        }

        // 3️⃣ Check Employee table to get role
        Optional<Employee> empOpt = employeeRepository.findByEmail(request.getEmail());
        if (empOpt.isPresent()) {
            Employee emp = empOpt.get();
            if (emp.getRole() != null && !emp.getRole().getRole().equals(user.getRole().getRole())) {
                // Sync User.role with Employee.role
                user.setRole(emp.getRole());
                userRepository.save(user);
            }
        }

        return user; // now role is synced with Employee.role
    }

    // ================= FORGOT PASSWORD =================
    public String forgotPassword(ForgotPasswordRequest request) {
        Optional<User> userOpt = userRepository.findByEmailIgnoreCase(request.getEmail());

        if (userOpt.isEmpty()) {
            return "USER_NOT_FOUND";
        }

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return "PASSWORD_MISMATCH";
        }

        User user = userOpt.get();
        user.setPassword(request.getNewPassword());
        userRepository.save(user);

        return "PASSWORD_UPDATED";
    }
}*/
