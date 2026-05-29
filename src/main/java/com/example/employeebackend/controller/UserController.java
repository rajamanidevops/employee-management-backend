package com.example.employeebackend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Map<String, Object> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {

        Map<String, Object> response = new HashMap<>();

        response.put("username", jwt.getClaim("preferred_username"));
        response.put("email", jwt.getClaim("email"));
        response.put("name", jwt.getClaim("name"));
        response.put("emailVerified", jwt.getClaim("email_verified"));

        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        response.put("roles", realmAccess != null ? realmAccess.get("roles") : null);

        return response;
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('USER')")
    public String userTest() {
        return "USER API WORKING ✅";
    }
}