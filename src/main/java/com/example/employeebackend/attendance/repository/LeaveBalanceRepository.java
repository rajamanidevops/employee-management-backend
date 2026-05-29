package com.example.employeebackend.attendance.repository;

import com.example.employeebackend.attendance.entity.LeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeaveBalanceRepository
        extends JpaRepository<LeaveBalance, Long> {

    Optional<LeaveBalance> findByEmail(String email);

    boolean existsByEmail(String email);
}