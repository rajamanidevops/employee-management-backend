package com.example.employeebackend.attendance.dto;

import com.example.employeebackend.attendance.entity.AttendanceStatus;
import java.time.LocalTime;

public class AdminAttendanceUpdateDto {

    private LocalTime checkIn;
    private LocalTime checkOut;
    private Integer workingMinutes;
    private AttendanceStatus status;

    public AdminAttendanceUpdateDto() {
    }

    public LocalTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalTime checkOut) {
        this.checkOut = checkOut;
    }

    public Integer getWorkingMinutes() {
        return workingMinutes;
    }

    public void setWorkingMinutes(Integer workingMinutes) {
        this.workingMinutes = workingMinutes;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }
}
