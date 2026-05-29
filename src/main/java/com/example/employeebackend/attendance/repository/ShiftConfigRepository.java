package com.example.employeebackend.attendance.repository;

import com.example.employeebackend.attendance.entity.ShiftConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShiftConfigRepository extends JpaRepository<ShiftConfig, Long> {
}