package com.example.employeebackend.attendance.service;

import com.example.employeebackend.attendance.entity.LeaveBalance;
import com.example.employeebackend.attendance.repository.LeaveBalanceRepository;
import com.example.employeebackend.entity.Employee;

import org.springframework.stereotype.Service;

@Service
public class LeaveBalanceService {

    private final LeaveBalanceRepository leaveBalanceRepo;

    public LeaveBalanceService(LeaveBalanceRepository leaveBalanceRepo) {
        this.leaveBalanceRepo = leaveBalanceRepo;
    }

    // ✅ CALL THIS WHEN EMPLOYEE IS CREATED
    public void createInitialBalance(Employee employee) {

        // ❌ Prevent duplicate balance
        if (leaveBalanceRepo.existsByEmail(employee.getEmail())) {
            return;
        }

        LeaveBalance balance = new LeaveBalance();
        balance.setEmail(employee.getEmail());
        balance.setClBalance(12);
        balance.setSlBalance(8);
        balance.setElBalance(15);

        leaveBalanceRepo.save(balance);
    }
}
