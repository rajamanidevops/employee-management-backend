package com.example.employeebackend.attendance.service;

import com.example.employeebackend.attendance.dto.AttendanceResponse;
import com.example.employeebackend.attendance.entity.*;
import com.example.employeebackend.attendance.repository.*;
import com.example.employeebackend.entity.EmployeeStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.employeebackend.entity.Employee;
import com.example.employeebackend.repository.EmployeeRepository;

import java.time.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepo;
    private final LeaveRepository leaveRepo;
    private final HolidayRepository holidayRepo;
    private final ShiftConfigRepository shiftRepo;
    private final EmployeeRepository employeeRepo;

    public AttendanceService(
            AttendanceRepository attendanceRepo,
            LeaveRepository leaveRepo,
            HolidayRepository holidayRepo,
            ShiftConfigRepository shiftRepo,
            EmployeeRepository employeeRepo) {

        this.attendanceRepo = attendanceRepo;
        this.leaveRepo = leaveRepo;
        this.holidayRepo = holidayRepo;
        this.shiftRepo = shiftRepo;
        this.employeeRepo = employeeRepo;
    }

    /* ================= CHECK-IN ================= */

    public Attendance checkIn(String email) {

        LocalDate today = LocalDate.now();

        // 1. WEEK OFF CHECK


// 1️⃣ HOLIDAY CHECK
        if (holidayRepo.existsByHolidayDate(today)) {
            throw new RuntimeException("Today is a holiday");
        }

// 2️⃣ WEEK OFF CHECK
        DayOfWeek day = today.getDayOfWeek();
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
            throw new RuntimeException("Today is a week off");
        }

// 3️⃣ LEAVE CHECK
        boolean onLeave = leaveRepo
                .findByEmailAndStatus(email, "APPROVED")
                .stream()
                .anyMatch(l ->
                        !today.isBefore(l.getFromDate()) &&
                                !today.isAfter(l.getToDate())
                );

        if (onLeave) {
            throw new RuntimeException("You are on approved leave today");
        }

// 4️⃣ DUPLICATE CHECK
        if (attendanceRepo.existsByEmailAndAttendanceDate(email, today)) {
            throw new RuntimeException("Already checked in");
        }

        // 4. EMPLOYEE
        Employee emp = employeeRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (emp.getStatus() != EmployeeStatus.ACTIVE) {
            throw new RuntimeException("Inactive employee cannot mark attendance");
        }

        // 5. SHIFT
        ShiftConfig shift = shiftRepo.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Shift not configured"));

        LocalTime now = LocalTime.now();

        Attendance att = new Attendance();
        att.setEmail(email);
        att.setDepartment(emp.getDepartment().getName());
        att.setAttendanceDate(today);
        att.setCheckInTime(now);
        att.setStatus(AttendanceStatus.PRESENT);

        LocalTime shiftStart = shift.getStartTime();

        if (now.isAfter(shiftStart)) {
            int lateMinutes = (int) Duration.between(shiftStart, now).toMinutes();
            att.setLateMinutes(lateMinutes);
        } else {
            att.setLateMinutes(0);
        }

        att.setEarlyExitMinutes(0);
        att.setIsAutoMarked(false);

        return attendanceRepo.save(att);
    }

    /* ================= CHECK-OUT ================= */
    @Transactional
    public Attendance checkOut(String email) {

        LocalDate today = LocalDate.now();

        Employee emp = employeeRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (emp.getStatus() != EmployeeStatus.ACTIVE) {
            throw new RuntimeException("Inactive employee cannot check out");
        }

        Attendance att = attendanceRepo
                .findByEmailAndAttendanceDate(email, today)
                .orElseThrow(() -> new RuntimeException("Check-in missing"));

        // 🔒 Prevent double checkout
        if (att.getCheckOutTime() != null) {
            throw new RuntimeException("Already checked out");
        }

        ShiftConfig shift = shiftRepo.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Shift not configured"));

        LocalTime now = LocalTime.now();
        att.setCheckOutTime(now);

        int workedMinutes = (int) Duration
                .between(att.getCheckInTime(), now)
                .toMinutes();

        att.setWorkingMinutes(workedMinutes);

        // 🕒 EARLY EXIT CALCULATION
        if (now.isBefore(shift.getEndTime())) {
            int earlyMinutes = (int) Duration
                    .between(now, shift.getEndTime())
                    .toMinutes();

            att.setEarlyExitMinutes(earlyMinutes);
        } else {
            att.setEarlyExitMinutes(0);
        }

        // ✅ STATUS
        if (workedMinutes >= shift.getFullDayMinutes()) {
            att.setStatus(AttendanceStatus.PRESENT);
        } else if (workedMinutes >= shift.getHalfDayMinutes()) {
            att.setStatus(AttendanceStatus.HALF_DAY);
        } else {
            att.setStatus(AttendanceStatus.ABSENT);
        }

        // ❌ DO NOT call save() inside @Transactional
        return att;
    }
    /* ================= LEAVE APPROVAL ================= */




    /* ================= DASHBOARD APIs (LIKE IMAGE) ================= */

    public long getPresentCount(String email) {
        return attendanceRepo.countByEmailAndStatus(email, AttendanceStatus.PRESENT);
    }

    public long getLateCount(String email) {
        return attendanceRepo.findByEmail(email)
                .stream()
                .filter(a -> a.getLateMinutes() != null && a.getLateMinutes() > 0)
                .count();
    }

    public long getAbsentCount(String email) {
        return attendanceRepo.countByEmailAndStatus(email, AttendanceStatus.ABSENT);
    }


    public long getLeaveCount(String email) {
        return leaveRepo.findByEmailAndStatus(email, "APPROVED").size();
    }


    public List<AttendanceResponse> monthly(String email) {

        Employee emp = employeeRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        List<Attendance> records = attendanceRepo.findByEmail(email);

        return records.stream().map(att -> {

            AttendanceResponse r = new AttendanceResponse();

            LocalDate attendanceDate = att.getAttendanceDate();

            // 🚫 AFTER LEAVING DATE → MARK INACTIVE / NO WORK
            if (emp.getDateOfLeaving() != null &&
                    attendanceDate.isAfter(emp.getDateOfLeaving())) {

                r.setAttendanceDate(attendanceDate.toString());
                r.setEmail(att.getEmail());
                r.setEmployeeName(emp.getName());
                r.setDepartment(emp.getDepartment().getName());

                r.setStatus("INACTIVE");
                r.setWorkingMinutes(0);
                r.setLateMinutes(0);
                r.setCheckInTime(null);
                r.setCheckOutTime(null);

                return r;
            }

            // ✅ NORMAL ATTENDANCE (ACTIVE PERIOD)
            int workingMinutes = 0;

            if (att.getCheckInTime() != null && att.getCheckOutTime() != null) {
                workingMinutes = (int) Duration
                        .between(att.getCheckInTime(), att.getCheckOutTime())
                        .toMinutes();
            }

            r.setAttendanceDate(attendanceDate.toString());
            r.setEmail(att.getEmail());
            r.setEmployeeName(emp.getName());
            r.setDepartment(att.getDepartment());

            r.setCheckInTime(att.getCheckInTime() != null ? att.getCheckInTime().toString() : null);
            r.setCheckOutTime(att.getCheckOutTime() != null ? att.getCheckOutTime().toString() : null);

            r.setWorkingMinutes(workingMinutes);
            r.setLateMinutes(att.getLateMinutes() != null ? att.getLateMinutes() : 0);
            r.setStatus(att.getStatus().name());

            return r;

        }).toList();
    }


    /* ================= HELPERS ================= */

    private Attendance createAttendance(String email, LocalDate date) {

        Employee emp = employeeRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Attendance a = new Attendance();
        a.setEmail(email);
        a.setDepartment(emp.getDepartment().getName()); // ✅ NO ERROR if setter exists
        a.setAttendanceDate(date);
        a.setStatus(AttendanceStatus.ABSENT);
        a.setWorkingMinutes(0);

        return a;
    }


    @Transactional
    public Attendance overrideAttendance(
            String managerEmail,
            String employeeEmail,
            LocalDate date,
            AttendanceStatus status,
            Integer workingMinutes
    ) {

        // 🔒 Validate employee exists
        Employee emp = employeeRepo.findByEmail(employeeEmail)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (emp.getStatus() != EmployeeStatus.ACTIVE) {
            throw new RuntimeException("Cannot override attendance for inactive employee");
        }

        // 🔒 Manager authorization (employee must report to manager)
        if (emp.getManager() != null &&
                !emp.getManager().getEmail().equals(managerEmail)) {
            throw new RuntimeException("Unauthorized override");
        }

        Attendance attendance = attendanceRepo
                .findByEmailAndAttendanceDate(employeeEmail, date)
                .orElseGet(() -> {
                    Attendance a = new Attendance();
                    a.setEmail(employeeEmail);
                    a.setDepartment(emp.getDepartment().getName());
                    a.setAttendanceDate(date);
                    return a;
                });

        attendance.setStatus(status);
        attendance.setWorkingMinutes(
                workingMinutes != null ? workingMinutes : 0
        );

        attendance.setCheckInTime(null);
        attendance.setCheckOutTime(null);
        attendance.setLateMinutes(0);
        attendance.setEarlyExitMinutes(0);
        attendance.setIsAutoMarked(false); // ❗ manager override

        return attendanceRepo.save(attendance);
    }

    public List<AttendanceResponse> getTeamAttendance(
            String managerEmail,
            LocalDate from,
            LocalDate to
    ) {

        // ✅ 1. Get manager entity
        Employee manager = employeeRepo.findByEmail(managerEmail)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        // 🔥 ADD THIS CHECK HERE
        List<Employee> team = employeeRepo.findByManager(manager)
                .stream()
                .filter(e -> e.getStatus() == EmployeeStatus.ACTIVE)
                .toList();

        if (team.isEmpty()) {
            throw new RuntimeException("No employees assigned to this manager");
        }

        // ✅ 2. Fetch attendance for team
        return team.stream()
                .flatMap(emp ->
                        attendanceRepo.findByEmail(emp.getEmail())
                                .stream()
                                .filter(a ->
                                        !a.getAttendanceDate().isBefore(from) &&
                                                !a.getAttendanceDate().isAfter(to)
                                )
                )
                .map(att -> {
                    AttendanceResponse r = new AttendanceResponse();

                    r.setAttendanceDate(att.getAttendanceDate().toString());
                    r.setEmail(att.getEmail());
                    r.setEmployeeName(att.getEmail().split("@")[0]);
                    r.setDepartment(att.getDepartment());

                    r.setCheckInTime(att.getCheckInTime() != null ? att.getCheckInTime().toString() : null);
                    r.setCheckOutTime(att.getCheckOutTime() != null ? att.getCheckOutTime().toString() : null);

                    r.setStatus(att.getStatus().name());
                    r.setWorkingMinutes(att.getWorkingMinutes());
                    r.setLateMinutes(att.getLateMinutes() != null ? att.getLateMinutes() : 0);

                    return r;
                })
                .toList();
    }

    public List<AttendanceResponse> getAutoMonthlyCalendar(
            String email,
            int year,
            int month
    ) {

        Employee emp = employeeRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (emp.getStatus() != EmployeeStatus.ACTIVE) {
            throw new RuntimeException("Inactive employee calendar is disabled");
        }


        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        List<Attendance> records = attendanceRepo.findByEmail(email);

        List<AttendanceResponse> result = new java.util.ArrayList<>();

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {

            final LocalDate currentDate = date;

            Attendance att = records.stream()
                    .filter(a -> a.getAttendanceDate() != null)
                    .filter(a -> a.getAttendanceDate().equals(currentDate))
                    .findFirst()
                    .orElse(null);

            AttendanceResponse r = new AttendanceResponse();
            r.setAttendanceDate(currentDate.toString());
            r.setEmail(email);
            r.setEmployeeName(emp.getName());
            r.setDepartment(emp.getDepartment().getName());

            // 🚫 AFTER LEAVING DATE → NO DATA (ADD HERE 👇)
            if (emp.getDateOfLeaving() != null &&
                    currentDate.isAfter(emp.getDateOfLeaving())) {

                r.setStatus("INACTIVE");
                result.add(r);
                continue;
            }

            /* ================= HOLIDAY ================= */
            boolean isHoliday = holidayRepo.findAll().stream()
                    .map(Holiday::getHolidayDate)
                    .filter(Objects::nonNull)
                    .anyMatch(d -> d.equals(currentDate));

            if (isHoliday) {
                r.setStatus("HOLIDAY");
                result.add(r);
                continue;
            }

            /* ================= WEEK OFF ================= */
            if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {

                r.setStatus("WEEK_OFF");
                result.add(r);
                continue;
            }

            /* ================= LEAVE ================= */
            boolean isLeave = leaveRepo
                    .findByEmailAndStatus(email, "APPROVED")
                    .stream()
                    .anyMatch(l ->
                            !currentDate.isBefore(l.getFromDate()) &&
                                    !currentDate.isAfter(l.getToDate())
                    );

            if (isLeave) {
                r.setStatus("LEAVE");
                result.add(r);
                continue;
            }

            /* ================= PRESENT / ABSENT ================= */
            if (att != null) {
                r.setStatus(att.getStatus().name());
                r.setCheckInTime(att.getCheckInTime() != null ? att.getCheckInTime().toString() : null);
                r.setCheckOutTime(att.getCheckOutTime() != null ? att.getCheckOutTime().toString() : null);
                r.setLateMinutes(att.getLateMinutes() != null ? att.getLateMinutes() : 0);
            } else {
                r.setStatus("ABSENT");
            }

            result.add(r);
        }

        return result;
    }

}