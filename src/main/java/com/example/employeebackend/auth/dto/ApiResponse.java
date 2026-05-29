package com.example.employeebackend.auth.dto;

public class ApiResponse {

    private boolean success;
    private String message;

    // optional fields (real project)
    private String token;
    private String role;

    // ===== Constructors =====

    // existing usage (no breaking changes)
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // extended usage (login / auth)
    public ApiResponse(boolean success, String message, String token, String role) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.role = role;
    }

    // ===== Getters & Setters =====

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
