package com.example.employeebackend.attendance.repository;

import com.example.employeebackend.attendance.entity.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

    // =========================
    // EMPLOYEE (SELF)
    // =========================
    List<Leave> findByEmail(String email);

    List<Leave> findByEmailAndStatus(
            String email,
            String status
    );

    boolean existsByEmailAndFromDateAndToDate(
            String email,
            LocalDate fromDate,
            LocalDate toDate
    );

    // =========================
    // MANAGER
    // =========================
    List<Leave> findByManagerEmailAndStatus(
            String managerEmail,
            String status
    );

    List<Leave> findByManagerEmail(
            String managerEmail
    );

    // =========================
    // ADMIN
    // =========================
    List<Leave> findByStatus(
            String status
    );
    List<Leave> findByEmailAndId(String email, Long id);

}