package com.example.employeebackend.attendance.service;

import com.example.employeebackend.attendance.entity.Attendance;
import com.example.employeebackend.attendance.entity.AttendanceStatus;
import com.example.employeebackend.attendance.entity.Leave;
import com.example.employeebackend.attendance.repository.AttendanceRepository;
import com.example.employeebackend.attendance.repository.LeaveRepository;
import com.example.employeebackend.entity.Employee;
import com.example.employeebackend.repository.EmployeeRepository;
import com.example.employeebackend.attendance.entity.LeaveBalance;
import com.example.employeebackend.attendance.repository.LeaveBalanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepo;
    private final AttendanceRepository attendanceRepo;
    private final EmployeeRepository employeeRepo;
    private final LeaveBalanceRepository leaveBalanceRepo;
    public LeaveService(
            LeaveRepository leaveRepo,
            AttendanceRepository attendanceRepo,
            EmployeeRepository employeeRepo,
            LeaveBalanceRepository leaveBalanceRepo
    ) {
        this.leaveRepo = leaveRepo;
        this.attendanceRepo = attendanceRepo;
        this.employeeRepo = employeeRepo;
        this.leaveBalanceRepo = leaveBalanceRepo;
    }

    /* ================= EMPLOYEE APPLY ================= */

    public Leave applyLeave(Leave leave) {

        if (leave.getFromDate().isAfter(leave.getToDate())) {
            throw new RuntimeException("Invalid date range");
        }

        // 1️⃣ Get employee
        Employee employee = employeeRepo
                .findByEmail(leave.getEmail())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employee.getManager() == null) {
            throw new RuntimeException("No manager assigned to employee");
        }

        if (employee.getManager().getEmail() == null) {
            throw new RuntimeException("Manager email missing");
        }

        leave.setManagerEmail(employee.getManager().getEmail());

        // 4️⃣ Set initial status
        leave.setStatus("PENDING");

        return leaveRepo.save(leave);
    }
    public List<Leave> getEmployeeLeaves(String email) {
        return leaveRepo.findByEmail(email);
    }

    /* ================= MANAGER VIEW ================= */

    public List<Leave> getPendingLeavesForManager(String managerEmail) {
        return leaveRepo.findByManagerEmailAndStatus(
                managerEmail,
                "PENDING"
        );
    }

    /* ================= APPROVE / REJECT ================= */

    @Transactional
    public void updateLeaveStatus(
            Long leaveId,
            String status,
            String managerEmail,
            String remarks   // ⭐ NEW
    ) {

        Leave leave = leaveRepo.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        if (leave.getManagerEmail() == null ||
                !leave.getManagerEmail().equalsIgnoreCase(managerEmail)) {
            throw new RuntimeException("Unauthorized");
        }

        leave.setStatus(status);
        leave.setApprovedBy(managerEmail);
        leave.setApprovedAt(LocalDateTime.now());

        // ⭐ ONLY STORE REJECTION REMARKS
        if ("REJECTED".equals(status)) {
            leave.setRejectionRemarks(remarks);
        }

        leaveRepo.save(leave);

        // ✅ ONLY FOR APPROVED
        if ("APPROVED".equals(status)) {

            validateLeaveBalance(leave);
            deductLeaveBalance(leave);
            autoMarkAttendance(leave);
        }
    }

    /* ================= AUTO MARK ATTENDANCE ================= */

    private void autoMarkAttendance(Leave leave) {



        LocalDate currentDate = leave.getFromDate();

        while (!currentDate.isAfter(leave.getToDate())) {

            final LocalDate loopDate = currentDate; // ✅ FINAL COPY

            Attendance att = attendanceRepo
                    .findByEmailAndAttendanceDate(
                            leave.getEmail(), loopDate
                    )
                    .orElseGet(() ->
                            createAttendance(
                                    leave.getEmail(), loopDate
                            )
                    );

            if ("HALF_DAY".equals(leave.getLeaveSession())) {

                att.setStatus(AttendanceStatus.HALF_DAY);
                att.setWorkingMinutes(240); // 4 hours

            } else {

                att.setStatus(AttendanceStatus.LEAVE);
                att.setWorkingMinutes(0);
            }

            att.setCheckInTime(null);
            att.setCheckOutTime(null);
            att.setIsAutoMarked(true);


            attendanceRepo.save(att);

            currentDate = currentDate.plusDays(1); // ✅ SAFE
        }
    }

    private void deductLeaveBalance(Leave leave) {

        LeaveBalance balance = leaveBalanceRepo
                .findByEmail(leave.getEmail())
                .orElseThrow();

        double days = calculateLeaveDays(leave);

        switch (leave.getLeaveType()) {
            case "CL" -> balance.setClBalance(balance.getClBalance() - days);
            case "SL" -> balance.setSlBalance(balance.getSlBalance() - days);
            case "EL" -> balance.setElBalance(balance.getElBalance() - days);
        }

        leaveBalanceRepo.save(balance);
    }

    private double calculateLeaveDays(Leave leave) {

        long days = leave.getFromDate()
                .datesUntil(leave.getToDate().plusDays(1))
                .count();
        if ("HALF_DAY".equals(leave.getLeaveSession())) {
            return 0.5;
        }
        return days;
    }
    private Attendance createAttendance(String email, LocalDate date) {

        Employee emp = employeeRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Attendance att = new Attendance();
        att.setEmail(email);
        att.setDepartment(emp.getDepartment().getName());
        att.setAttendanceDate(date);
        att.setStatus(AttendanceStatus.LEAVE);
        att.setWorkingMinutes(0);
        att.setIsAutoMarked(true);

        return att;
    }

    @Transactional
    public void cancelLeave(Long leaveId, String email) {

        Leave leave = leaveRepo.findByEmailAndId(email, leaveId)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Leave not found"));

        if ("REJECTED".equals(leave.getStatus())) {
            throw new RuntimeException("Rejected leave cannot be cancelled");
        }

        if ("APPROVED".equals(leave.getStatus())) {
            removeAutoMarkedAttendance(leave);
            restoreLeaveBalance(leave); // ✅ ADD THIS
        }

        leaveRepo.delete(leave);
    }
    private void removeAutoMarkedAttendance(Leave leave) {

        LocalDate date = leave.getFromDate();

        while (!date.isAfter(leave.getToDate())) {

            LocalDate loopDate = date;

            attendanceRepo.findByEmailAndAttendanceDate(
                    leave.getEmail(), loopDate
            ).ifPresent(att -> {
                if (att.getIsAutoMarked()) {
                    attendanceRepo.delete(att);
                }
            });

            date = date.plusDays(1);
        }
    }
    private void restoreLeaveBalance(Leave leave) {

        LeaveBalance balance = leaveBalanceRepo
                .findByEmail(leave.getEmail())
                .orElseThrow();

        double days = calculateLeaveDays(leave);

        switch (leave.getLeaveType()) {
            case "CL" -> balance.setClBalance(balance.getClBalance() + days);
            case "SL" -> balance.setSlBalance(balance.getSlBalance() + days);
            case "EL" -> balance.setElBalance(balance.getElBalance() + days);
        }

        leaveBalanceRepo.save(balance);
    }

    private void validateLeaveBalance(Leave leave) {

        LeaveBalance balance = leaveBalanceRepo
                .findByEmail(leave.getEmail())
                .orElseThrow();

        double days = calculateLeaveDays(leave);

        switch (leave.getLeaveType()) {
            case "CL" -> {
                if (balance.getClBalance() < days)
                    throw new RuntimeException("Insufficient CL balance");
            }
            case "SL" -> {
                if (balance.getSlBalance() < days)
                    throw new RuntimeException("Insufficient SL balance");
            }
            case "EL" -> {
                if (balance.getElBalance() < days)
                    throw new RuntimeException("Insufficient EL balance");
            }
        }
    }

    public List<Leave> getAllLeavesForManager(String managerEmail) {
        return leaveRepo.findByManagerEmail(managerEmail);
    }

    public Map<String, Long> getManagerSummary(String managerEmail) {

        List<Leave> leaves = leaveRepo.findByManagerEmail(managerEmail);

        return Map.of(
                "pending", leaves.stream().filter(l -> "PENDING".equals(l.getStatus())).count(),
                "approved", leaves.stream().filter(l -> "APPROVED".equals(l.getStatus())).count(),
                "rejected", leaves.stream().filter(l -> "REJECTED".equals(l.getStatus())).count()
        );
    }
}