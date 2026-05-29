package com.example.employeebackend.controller;
import com.example.employeebackend.entity.EmployeeStatus;
import com.example.employeebackend.entity.Employee;
import com.example.employeebackend.repository.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final EmployeeRepository employeeRepository;

    public AdminController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/test")
    public String adminTest() {
        return "ADMIN API WORKING ✅";
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeRepository.findAll());
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/employees/by-email")
    public ResponseEntity<Employee> getEmployeeByEmail(@RequestParam String email) {
        return employeeRepository.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok(employeeRepository.save(employee));
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee updatedEmployee) {

        return employeeRepository.findById(id)
                .map(emp -> {
                    emp.setName(updatedEmployee.getName());
                    emp.setEmail(updatedEmployee.getEmail());
                    emp.setEmpNo(updatedEmployee.getEmpNo());
                    emp.setStatus(updatedEmployee.getStatus());
                    emp.setDepartment(updatedEmployee.getDepartment());
                    return ResponseEntity.ok(employeeRepository.save(emp));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Long id) {
        if (!employeeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        employeeRepository.deleteById(id);
        return ResponseEntity.ok("Employee deleted successfully ✅");
    }

    @PutMapping("/employees/{id}/status")
    public ResponseEntity<Employee> updateEmployeeStatus(
            @PathVariable Long id,
            @RequestParam EmployeeStatus status) {

        return employeeRepository.findById(id)
                .map(emp -> {
                    emp.setStatus(status);
                    return ResponseEntity.ok(employeeRepository.save(emp));
                })
                .orElse(ResponseEntity.notFound().build());
    }


}