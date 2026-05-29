package com.example.employeebackend.attendance.entity;

import jakarta.persistence.*;
import java.time.*;

@Entity
@Table(
        name = "attendance",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"email", "attendance_date"})
        }
)
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String department;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    private LocalTime checkInTime;
    private LocalTime checkOutTime;

    private Integer workingMinutes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    private Integer lateMinutes;
    private Integer earlyExitMinutes;

    @Column(nullable = false)
    private Boolean isAutoMarked = false;

    private String remarks;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



    @PrePersist
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ===== GETTERS & SETTERS =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDate getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public LocalTime getCheckInTime() { return checkInTime; }
    public void setCheckInTime(LocalTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalTime getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public Integer getWorkingMinutes() { return workingMinutes; }
    public void setWorkingMinutes(Integer workingMinutes) {
        this.workingMinutes = workingMinutes;
    }

    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public Integer getLateMinutes() { return lateMinutes; }
    public void setLateMinutes(Integer lateMinutes) {
        this.lateMinutes = lateMinutes;
    }

    public Integer getEarlyExitMinutes() { return earlyExitMinutes; }
    public void setEarlyExitMinutes(Integer earlyExitMinutes) {
        this.earlyExitMinutes = earlyExitMinutes;
    }

    public Boolean getIsAutoMarked() { return isAutoMarked; }
    public void setIsAutoMarked(Boolean autoMarked) {
        isAutoMarked = autoMarked;
    }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}