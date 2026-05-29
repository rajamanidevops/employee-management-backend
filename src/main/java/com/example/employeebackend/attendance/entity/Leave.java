package com.example.employeebackend.attendance.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "employee_leave")
public class Leave {

    // =========================
    // PRIMARY KEY
    // =========================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // EMPLOYEE INFO
    // =========================
    @Column(nullable = false)
    private String email; // employee email

    // =========================
    // LEAVE DETAILS
    // =========================
    @Column(nullable = false)
    private String leaveType; // CL / SL / EL

    @Column(nullable = false)
    private LocalDate fromDate;

    @Column(nullable = false)
    private LocalDate toDate;

    @Column(nullable = false)
    private String status; // PENDING / APPROVED / REJECTED

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(nullable = false)
    private String leaveSession; // FULL / FIRST_HALF / SECOND_HALF

    // =========================
    // MANAGER APPROVAL INFO
    // =========================
    @Column(nullable = false)
    private String managerEmail; // who should approve

    private String approvedBy; // manager email
    private LocalDateTime approvedAt;

    @Column(columnDefinition = "TEXT")
    private String rejectionRemarks;

    // =========================
    // GETTERS & SETTERS
    // =========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public String getLeaveSession() {
        return leaveSession;
    }


    public void setLeaveSession(String leaveSession) {
        this.leaveSession = leaveSession;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public String getRejectionRemarks() {
        return rejectionRemarks;
    }

    public void setRejectionRemarks(String rejectionRemarks) {
        this.rejectionRemarks = rejectionRemarks;
    }
}