package com.example.employeebackend.attendance.dto;

public class LeaveActionRequest {

    private String status;   // APPROVED / REJECTED
    private String remarks;  // rejection reason

    public LeaveActionRequest() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
