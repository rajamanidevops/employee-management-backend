package com.example.employeebackend.attendance.scheduler;

import com.example.employeebackend.attendance.entity.*;
import com.example.employeebackend.attendance.repository.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
public class AttendanceScheduler {

    private final AttendanceRepository attendanceRepo;
    private final HolidayRepository holidayRepo;

    public AttendanceScheduler(
            AttendanceRepository attendanceRepo,
            HolidayRepository holidayRepo) {

        this.attendanceRepo = attendanceRepo;
        this.holidayRepo = holidayRepo;
    }

    /**
     * ⏰ Runs daily at 11:59 PM
     */
    @Transactional
    @Scheduled(cron = "0 59 23 * * ?")
    public void autoMarkAbsent() {

        LocalDate today = LocalDate.now();

        // ❌ Skip holiday
        if (holidayRepo.existsByHolidayDate(today)) {
            return;
        }

        List<Attendance> todayAttendance =
                attendanceRepo.findByAttendanceDate(today);

        for (Attendance att : todayAttendance) {

            // ❌ User checked in → do nothing
            if (att.getCheckInTime() != null) {
                continue;
            }

            // ❌ Already auto-marked
            if (Boolean.TRUE.equals(att.getIsAutoMarked())) {
                continue;
            }

            // ✅ Auto-absent
            att.setStatus(AttendanceStatus.ABSENT);
            att.setWorkingMinutes(0);
            att.setIsAutoMarked(true);
            att.setRemarks("Auto marked absent");

            attendanceRepo.save(att);
        }
    }
}