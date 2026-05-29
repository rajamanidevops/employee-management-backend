package com.example.employeebackend.attendance.service;
import com.example.employeebackend.attendance.dto.AdminAttendanceUpdateDto;
import com.example.employeebackend.attendance.entity.Attendance;
import com.example.employeebackend.attendance.repository.AttendanceRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
@Service
public class AdminAttendanceService {

    private final AttendanceRepository attendanceRepo;

    public AdminAttendanceService(AttendanceRepository attendanceRepo) {
        this.attendanceRepo = attendanceRepo;
    }

    public Page<Attendance> getAttendanceList(
            LocalDate fromDate,
            LocalDate toDate,
            String department,
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size, Sort.by("attendanceDate").descending());

        // ✅ 1. NO FILTERS → ALL DATA
        if (fromDate == null && toDate == null && department == null) {
            return attendanceRepo.findAll(pageable);
        }

        // ✅ 2. DATE FILTER ONLY
        if (department == null || department.isBlank() || department.equalsIgnoreCase("ALL")) {
            return attendanceRepo.findByAttendanceDateBetween(
                    fromDate,
                    toDate,
                    pageable
            );
        }

        // ✅ 3. DATE + DEPARTMENT FILTER
        return attendanceRepo.findByAttendanceDateBetweenAndDepartment(
                fromDate,
                toDate,
                department,
                pageable
        );
    }
    public List<Attendance> getMonthlyAttendance(
            String email,
            int month,
            int year
    ) {
        return attendanceRepo.findMonthly(email, month, year);
    }

    public void exportCsv(
            LocalDate fromDate,
            LocalDate toDate,
            String department,
            PrintWriter writer
    ) {

        List<Attendance> list;

        if (department == null || department.isBlank()) {
            list = attendanceRepo.findByAttendanceDateBetween(fromDate, toDate);
        } else {
            list = attendanceRepo.findByAttendanceDateBetweenAndDepartment(
                    fromDate, toDate, department);
        }

        // HEADER
        writer.println("Date,Email,Department,CheckIn,CheckOut,WorkingMinutes,Status");

        for (Attendance a : list) {

            String checkIn = a.getCheckInTime() != null
                    ? a.getCheckInTime().toString()
                    : "--";

            String checkOut = a.getCheckOutTime() != null
                    ? a.getCheckOutTime().toString()
                    : "--";

            String working = a.getWorkingMinutes() != null
                    ? String.valueOf(a.getWorkingMinutes())
                    : "0";

            writer.println(
                    a.getAttendanceDate() + "," +
                            a.getEmail() + "," +
                            a.getDepartment() + "," +
                            checkIn + "," +
                            checkOut + "," +
                            working + "," +
                            a.getStatus()
            );
        }
    }
    public Attendance updateAttendance(
            Long id,
            AdminAttendanceUpdateDto dto
    ) {

        Attendance attendance = attendanceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found"));

        if (dto.getCheckIn() != null && dto.getCheckOut() != null &&
                dto.getCheckOut().isBefore(dto.getCheckIn())) {
            throw new RuntimeException("Check-out time cannot be before check-in time");
        }

        attendance.setCheckInTime(dto.getCheckIn());
        attendance.setCheckOutTime(dto.getCheckOut());

        if (dto.getCheckIn() != null && dto.getCheckOut() != null) {
            int minutes = (int) java.time.Duration
                    .between(dto.getCheckIn(), dto.getCheckOut())
                    .toMinutes();
            attendance.setWorkingMinutes(minutes);
        } else {
            attendance.setWorkingMinutes(dto.getWorkingMinutes());
        }

        attendance.setStatus(dto.getStatus());

        // IMPORTANT: reset system-calculated fields
        attendance.setLateMinutes(0);
        attendance.setEarlyExitMinutes(0);
        attendance.setIsAutoMarked(false);

        return attendanceRepo.save(attendance);
    }
}