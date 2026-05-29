/*package com.example.employeebackend.service;

import com.example.employeebackend.entity.Employee;
import com.example.employeebackend.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepo;

    public EmployeeService(EmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    // =========================
    // READ
    // =========================
    public List<Employee> findAll() {
        return employeeRepo.findByDeletedFalse();
    }

    public List<Employee> findAllActive() {
        return employeeRepo.findByActiveTrueAndDeletedFalse();
    }

    public List<Employee> findAllInactive() {
        return employeeRepo.findByActiveFalseAndDeletedFalse();
    }

    public Employee findActiveById(Long id) {
        return employeeRepo.findByIdAndActiveTrueAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Active employee not found"));
    }

    public Employee findById(Long id) {
        return employeeRepo.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    // =========================
    // SAVE
    // =========================
    public Employee save(Employee employee) {
        if (employee.getActive() == null) employee.setActive(true);
        if (employee.getDeleted() == null) employee.setDeleted(false);
        return employeeRepo.save(employee);
    }

    // =========================
    // SOFT DELETE
    // =========================
    public void delete(Long id) {
        Employee emp = employeeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        emp.setDeleted(true);
        employeeRepo.save(emp);
    }

    // =========================
    // COUNTS
    // =========================
    public long activeCount() {
        return employeeRepo.countByActiveTrueAndDeletedFalse();
    }

    public long inactiveCount() {
        return employeeRepo.countByActiveFalseAndDeletedFalse();
    }

    public long countEmployeesCreatedBetween(
            LocalDateTime start,
            LocalDateTime end) {

        return employeeRepo.countByCreatedAtBetween(start, end);
    }
}*/
package com.example.employeebackend.service;

import com.example.employeebackend.attendance.service.LeaveBalanceService;
import com.example.employeebackend.entity.Employee;
import com.example.employeebackend.entity.EmployeeStatus;
import com.example.employeebackend.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;


@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepo;
    private final LeaveBalanceService leaveBalanceService;
    public EmployeeService(
            EmployeeRepository employeeRepo,
            LeaveBalanceService leaveBalanceService // ✅ ADD THIS
    ) {
        this.employeeRepo = employeeRepo;
        this.leaveBalanceService = leaveBalanceService; // ✅ ADD THIS
    }

    // =========================
    // READ
    // =========================
    public List<Employee> findAll() {
        return employeeRepo.findAll();
    }

    public List<Employee> findAllActive() {
        return employeeRepo.findByDateOfLeavingIsNull();
    }

    public List<Employee> findAllInactive() {
        return employeeRepo.findByDateOfLeavingIsNotNull();
    }

    public Employee findActiveById(Long id) {
        return employeeRepo.findById(id)
                .filter(e -> e.getStatus() == EmployeeStatus.ACTIVE)
                .orElseThrow(() -> new RuntimeException("Active employee not found"));
    }

    public Employee findById(Long id) {
        return employeeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    // =========================
    // SAVE
    // =========================
    public Employee save(Employee employee) {

        if (employee.getDateOfLeaving() != null &&
                employee.getDateOfJoining() != null &&
                employee.getDateOfLeaving().isBefore(employee.getDateOfJoining())) {
            throw new RuntimeException("Date of leaving cannot be before date of joining");
        }

        // 🔥 FIX: resolve manager properly
        String managerEmpNo = null;

        if (employee.getManager() != null && employee.getManager().getEmpNo() != null) {
            managerEmpNo = employee.getManager().getEmpNo();
        }

        if (managerEmpNo != null) {

            Employee manager = resolveManagerByEmpNo(managerEmpNo);

            if (manager.getStatus() != EmployeeStatus.ACTIVE) {
                throw new RuntimeException("Inactive employee cannot be a manager");
            }

            employee.setManager(manager);

        } else {
            employee.setManager(null);
        }

        // 1️⃣ Save employee first
        Employee savedEmployee = employeeRepo.save(employee);

        // 2️⃣ Create initial leave balance
        leaveBalanceService.createInitialBalance(savedEmployee);

        // 3️⃣ Return saved employee
        return savedEmployee;
    }
    // =========================
    // HARD DELETE ✅
    // =========================
    public void delete(Long id) {
        employeeRepo.deleteById(id);
    }

    // =========================
    // COUNTS (CORRECT)
    // =========================
    public long activeCount() {
        return employeeRepo.countCurrentlyActive();
    }

    public long inactiveCount() {
        return employeeRepo.countCurrentlyInactive();
    }
    public List<Employee> getEmployeesForExport(
            EmployeeStatus status,
            String quickFilter,
            LocalDate fromDate,
            LocalDate toDate
    ) {



        List<Employee> employees = employeeRepo.findAll();

        // ACTIVE FILTER (based on leaving date)
        if (status != null) {
            employees = employees.stream()
                    .filter(e -> e.getStatus() == status)
                    .toList();
        }
        // DATE FILTER (JOINING DATE FILTER BEST PRACTICE)
        if (fromDate != null && toDate != null) {
            employees = employees.stream()
                    .filter(e -> e.getDateOfJoining() != null &&
                            !e.getDateOfJoining().isBefore(fromDate) &&
                            !e.getDateOfJoining().isAfter(toDate))
                    .toList();
        }

        return employeeRepo.findForExport(status, fromDate, toDate);
    }

    private Employee resolveManagerByEmpNo(String empNo) {
        if (empNo == null) return null;

        return employeeRepo.findByEmpNo(empNo)
                .orElseThrow(() -> new RuntimeException("Manager not found: " + empNo));
    }




    public Employee findByEmpNo(String empNo) {
        return employeeRepo.findByEmpNo(empNo)
                .orElseThrow(() -> new RuntimeException("Employee not found: " + empNo));
    }
}