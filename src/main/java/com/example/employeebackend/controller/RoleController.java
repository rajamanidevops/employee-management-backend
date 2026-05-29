package com.example.employeebackend.controller;

import com.example.employeebackend.entity.Role;
import com.example.employeebackend.repository.RoleRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@CrossOrigin(origins = "http://localhost:4200")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // ✅ CREATE ROLE (POSTMAN FIX)
    @PostMapping
    public Role createRole(@RequestBody Role role) {
        return roleRepository.save(role);
    }

    // (Optional) GET ALL ROLES
    @GetMapping
    public Iterable<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}

