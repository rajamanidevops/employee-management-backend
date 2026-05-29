package com.example.employeebackend.repository;

import com.example.employeebackend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    // ✅ REQUIRED for AuthService
    Optional<Role> findByRole(String role);
}

