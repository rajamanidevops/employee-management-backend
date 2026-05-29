package com.example.employeebackend.attendance.entity;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "shift_config")
public class ShiftConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime startTime;
    private LocalTime endTime;

    private Integer graceMinutes;
    private Integer fullDayMinutes;
    private Integer halfDayMinutes;

    // ===== GETTERS & SETTERS =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Integer getGraceMinutes() { return graceMinutes; }
    public void setGraceMinutes(Integer graceMinutes) {
        this.graceMinutes = graceMinutes;
    }

    public Integer getFullDayMinutes() { return fullDayMinutes; }
    public void setFullDayMinutes(Integer fullDayMinutes) {
        this.fullDayMinutes = fullDayMinutes;
    }

    public Integer getHalfDayMinutes() { return halfDayMinutes; }
    public void setHalfDayMinutes(Integer halfDayMinutes) {
        this.halfDayMinutes = halfDayMinutes;
    }
}