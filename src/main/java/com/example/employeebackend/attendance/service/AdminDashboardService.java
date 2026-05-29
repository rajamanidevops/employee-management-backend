package com.example.employeebackend.attendance.service;

import com.example.employeebackend.attendance.dto.AdminDashboardCardsDto;
import com.example.employeebackend.attendance.entity.Attendance;
import com.example.employeebackend.attendance.entity.AttendanceStatus;
import com.example.employeebackend.attendance.repository.AttendanceRepository;
import com.example.employeebackend.repository.EmployeeRepository;
import com.example.employeebackend.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
public class AdminDashboardService {

    private final AttendanceRepository attendanceRepo;
    private final EmployeeRepository employeeRepo;
    private final DepartmentRepository departmentRepo;

    public AdminDashboardService(
            AttendanceRepository attendanceRepo,
            EmployeeRepository employeeRepo,
            DepartmentRepository departmentRepo) {

        this.attendanceRepo = attendanceRepo;
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
    }

    // =========================
    // DASHBOARD TOP CARDS
    // =========================
    public AdminDashboardCardsDto getDashboardCards() {

        LocalDate today = LocalDate.now();
        if (isWeekend(today)) {
            long totalEmployees = employeeRepo.countCurrentlyActive();
            long totalDepartments = departmentRepo.count();

            return new AdminDashboardCardsDto(
                    totalEmployees,
                    totalDepartments,
                    0,
                    0,
                    0,
                    0,
                    0
            );
        }

        // ✅ FIX: DATE-BASED ACTIVE COUNT
        long totalEmployees = employeeRepo.countCurrentlyActive();
        long totalDepartments = departmentRepo.count();

        List<Attendance> todayList =
                attendanceRepo.findByAttendanceDate(today);

        long present = 0;
        long leave = 0;
        long late = 0;
        long halfDay = 0;
        long weekOff = 0;

        for (Attendance a : todayList) {

            if (a.getStatus() == null) continue;

            switch (a.getStatus()) {

                case PRESENT -> present++;

                case LATE -> {
                    present++;
                    late++;
                }

                case HALF_DAY -> {
                    present++;
                    halfDay++;
                }

                case LEAVE -> leave++;
                case WEEK_OFF -> weekOff++;
            }
        }

        long activeEmployees = totalEmployees - weekOff;

        long absent = activeEmployees - present - leave;

        if (absent < 0) absent = 0;


        return new AdminDashboardCardsDto(
                totalEmployees,
                totalDepartments,
                present,
                absent,
                leave,
                late,
                halfDay
        );
    }

    // =========================
    // DONUT CHART DATA
    // =========================
    public AdminDashboardCardsDto.TodayAttendanceDonutDto getTodayDonut() {

        LocalDate today = LocalDate.now();

        // ✅ FIX HERE TOO
        long totalEmployees = employeeRepo.countCurrentlyActive();

        List<Attendance> list =
                attendanceRepo.findByAttendanceDate(today);

        long present = 0;
        long leave = 0;
        long halfDay = 0;
        long weekOff = 0;

        for (Attendance a : list) {

            if (a.getStatus() == null) continue;

            if (a.getStatus() == AttendanceStatus.PRESENT ||
                    a.getStatus() == AttendanceStatus.LATE ||
                    a.getStatus() == AttendanceStatus.HALF_DAY) {

                present++;
            }

            if (a.getStatus() == AttendanceStatus.LEAVE) {
                leave++;
            }

            if (a.getStatus() == AttendanceStatus.HALF_DAY) {
                halfDay++;
            }
            if (a.getStatus() == AttendanceStatus.WEEK_OFF) {
                weekOff++;
            }
        }

        long activeEmployees = totalEmployees - weekOff;

        long absent = activeEmployees - present - leave;

        if (absent < 0) absent = 0;

        return new AdminDashboardCardsDto.TodayAttendanceDonutDto(
                present,
                absent,
                leave,
                halfDay
        );
    }
    private boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
}