package com.example.employeebackend.attendance.dto;

import java.time.LocalDate;

public class AttendanceOverrideRequest {

    private String employeeEmail;
    private LocalDate date;
    private String status; // PRESENT, ABSENT, HALF_DAY, LEAVE
    private Integer workingMinutes;

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getWorkingMinutes() {
        return workingMinutes;
    }

    public void setWorkingMinutes(Integer workingMinutes) {
        this.workingMinutes = workingMinutes;
    }
}
