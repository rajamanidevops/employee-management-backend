package com.example.employeebackend.attendance.dto;
import java.time.LocalDate;
public class LeaveRequest {

    private String email;
    private String leaveType;
    private String fromDate;
    private String toDate;
    private String reason;

    // No-arg constructor
    public LeaveRequest() {
    }

    // All-args constructor
    public LeaveRequest(String email, String leaveType, String fromDate, String toDate, String reason) {
        this.email = email;
        this.leaveType = leaveType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reason = reason;
    }

    // Getters and Setters
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

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}