package com.example.employeebackend.attendance.repository;

import com.example.employeebackend.attendance.entity.Attendance;
import com.example.employeebackend.attendance.entity.AttendanceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByEmailAndAttendanceDate(String email, LocalDate date);

    boolean existsByEmailAndAttendanceDate(String email, LocalDate date);

    List<Attendance> findByEmail(String email);

    List<Attendance> findByAttendanceDate(LocalDate date);

    long countByEmailAndStatus(String email, AttendanceStatus status);

    long countByAttendanceDateAndStatus(
            LocalDate date,
            AttendanceStatus status
    );

    // ✅ REQUIRED FOR ADMIN TABLE
    Page<Attendance> findByAttendanceDateBetween(
            LocalDate fromDate,
            LocalDate toDate,
            Pageable pageable
    );

    Page<Attendance> findByAttendanceDateBetweenAndDepartment(
            LocalDate fromDate,
            LocalDate toDate,
            String department,
            Pageable pageable
    );
    @Query("""
SELECT a FROM Attendance a
WHERE a.email = :email
AND MONTH(a.attendanceDate) = :month
AND YEAR(a.attendanceDate) = :year
ORDER BY a.attendanceDate
""")
    List<Attendance> findMonthly(
            String email,
            int month,
            int year
    );

    List<Attendance> findByAttendanceDateBetween(
            LocalDate fromDate,
            LocalDate toDate
    );

    List<Attendance> findByAttendanceDateBetweenAndDepartment(
            LocalDate fromDate,
            LocalDate toDate,
            String department
    );

}