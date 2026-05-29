package com.example.employeebackend.attendance.dto;

import java.io.Serializable;

public class AttendanceResponse implements Serializable {

    private String attendanceDate;

    private String email;           // ✅ ADDED
    private String employeeName;    // ✅ ADDED (optional but recommended)
    private String department;      // ✅ ADDED

    private String checkInTime;
    private String checkOutTime;

    private Integer workingMinutes;
    private Integer lateMinutes;

    private String status;

    public AttendanceResponse() {}

    public AttendanceResponse(
            String attendanceDate,
            String email,
            String employeeName,
            String department,
            String checkInTime,
            String checkOutTime,
            Integer workingMinutes,
            Integer lateMinutes,
            String status
    ) {
        this.attendanceDate = attendanceDate;
        this.email = email;
        this.employeeName = employeeName;
        this.department = department;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.workingMinutes = workingMinutes;
        this.lateMinutes = lateMinutes;
        this.status = status;
    }

    // ================= GETTERS & SETTERS =================

    public String getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public Integer getWorkingMinutes() {
        return workingMinutes;
    }

    public void setWorkingMinutes(Integer workingMinutes) {
        this.workingMinutes = workingMinutes;
    }

    public Integer getLateMinutes() {
        return lateMinutes;
    }

    public void setLateMinutes(Integer lateMinutes) {
        this.lateMinutes = lateMinutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}