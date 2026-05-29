package com.example.employeebackend.controller;

import com.example.employeebackend.dto.EmployeeResponse;
import com.example.employeebackend.entity.Employee;
import com.example.employeebackend.entity.EmployeeStatus;
import com.example.employeebackend.entity.Role;
import com.example.employeebackend.repository.DepartmentRepository;
import com.example.employeebackend.repository.RoleRepository;
import com.example.employeebackend.service.EmployeeExcelService;
import com.example.employeebackend.service.EmployeeService;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "http://localhost:4200")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeExcelService excelService;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;

    public EmployeeController(EmployeeService employeeService,
                              EmployeeExcelService excelService,
                              DepartmentRepository departmentRepository,
                              RoleRepository roleRepository) {
        this.employeeService = employeeService;
        this.excelService = excelService;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
    }

    /* =========================
       ROLE FROM JWT (ONLY FOR ACCESS CONTROL)
       ========================= */
    private String extractRole(Jwt jwt) {
        Map<String, Object> realmAccess =
                (Map<String, Object>) jwt.getClaims().get("realm_access");

        if (realmAccess == null) return "USER";

        List<String> roles = (List<String>) realmAccess.get("roles");

        return roles != null && roles.contains("ADMIN") ? "ADMIN" : "USER";
    }

    /* =========================
       GET ALL EMPLOYEES
       ========================= */
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getEmployees(
            @AuthenticationPrincipal Jwt jwt) {

        String authRole = extractRole(jwt);
        String email = jwt.getClaimAsString("email");

        List<Employee> employees;

        if ("ADMIN".equals(authRole)) {
            employees = employeeService.findAll();
        } else {
            Employee user = employeeService.findAll()
                    .stream()
                    .filter(e -> e.getEmail().equals(email))
                    .findFirst()
                    .orElse(null);

            if (user == null) {
                return ResponseEntity.ok(List.of());
            }

            // ✅ return only self + team under manager
            employees = employeeService.findAll()
                    .stream()
                    .filter(e ->
                            e.getEmail().equals(email) ||
                                    (e.getManager() != null &&
                                            e.getManager().getEmpNo().equals(user.getEmpNo()))
                    )
                    .toList();
        }

        return ResponseEntity.ok(
                employees.stream().map(EmployeeResponse::new).toList()
        );
    }
    /* =========================
       GET BY ID
       ========================= */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        String role = extractRole(jwt);
        String email = jwt.getClaimAsString("email");

        Employee emp = employeeService.findById(id); // only active + not deleted

        if (!"ADMIN".equals(role) && !emp.getEmail().equals(email)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(new EmployeeResponse(emp));
    }

    /* =========================
       CREATE EMPLOYEE (ROLE FROM FRONTEND DB)
       ========================= */
    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(
            @RequestBody Employee employee,
            @AuthenticationPrincipal Jwt jwt) {

        if (!"ADMIN".equals(extractRole(jwt))) {
            return ResponseEntity.status(403).build();
        }

        if (employeeService.findAll().stream()
                .anyMatch(e -> e.getEmail().equalsIgnoreCase(employee.getEmail()))) {
            return ResponseEntity.badRequest().build();
        }

        var department = departmentRepository.findById(
                        employee.getDepartment().getDeptId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Role role = roleRepository.findById(
                        employee.getRole().getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        employee.setDepartment(department);
        employee.setRole(role);





        // ✅ ADD THIS
        if (employee.getDateOfJoining() == null) {
            employee.setDateOfJoining(LocalDate.now());

        }

            /* =========================
       DATE OF LEAVING (OPTIONAL)
       ========================= */
        if (employee.getDateOfLeaving() != null) {

            if (employee.getDateOfLeaving()
                    .isBefore(employee.getDateOfJoining())) {

                return ResponseEntity.badRequest()
                        .body(null); // invalid date logic
            }
            // ⚠️ DO NOT set active here
            // Entity lifecycle hook will handle it
        }

        if (employee.getDepartment() == null || employee.getRole() == null) {
            return ResponseEntity.badRequest().body(null);
        }

// ===== MANAGER RESOLUTION (FIX) =====
        if (employee.getManager() != null && employee.getManager().getEmpNo() != null) {

            Employee manager = employeeService.findByEmpNo(
                    employee.getManager().getEmpNo()
            );

            employee.setManager(manager); // attach managed entity
        } else {
            employee.setManager(null);
        }
        Employee saved = employeeService.save(employee);
        return ResponseEntity.ok(new EmployeeResponse(saved));
    }

    /* =========================
       UPDATE EMPLOYEE
       ========================= */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee,
            @AuthenticationPrincipal Jwt jwt) {

        if (!"ADMIN".equals(extractRole(jwt))) {
            return ResponseEntity.status(403).build();
        }

        Employee existing = employeeService.findById(id);

        var department = departmentRepository.findById(
                        employee.getDepartment().getDeptId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Role role = roleRepository.findById(
                        employee.getRole().getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // ===== MANAGER UPDATE FIX =====
        if (employee.getManager() != null && employee.getManager().getEmpNo() != null) {

            Employee manager = employeeService.findByEmpNo(
                    employee.getManager().getEmpNo()
            );

            existing.setManager(manager);
        } else {
            existing.setManager(null);
        }



        existing.setEmpNo(employee.getEmpNo());
        existing.setName(employee.getName());
        existing.setEmail(employee.getEmail());
        existing.setDepartment(department);
        existing.setRole(role);

        existing.setDateOfJoining(employee.getDateOfJoining());
        existing.setDateOfLeaving(employee.getDateOfLeaving());


        return ResponseEntity.ok(
                new EmployeeResponse(employeeService.save(existing))
        );
    }

    /* =========================
       DELETE EMPLOYEE
       ========================= */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {

        if (!"ADMIN".equals(extractRole(jwt))) {
            return ResponseEntity.status(403).build();
        }

        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /* =========================
       EXPORT
       ========================= */
    @GetMapping("/export")
    public ResponseEntity<InputStreamResource> exportEmployees(
            @RequestParam(required = false) EmployeeStatus status,
            @RequestParam(required = false) String quickFilter,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @AuthenticationPrincipal Jwt jwt
    ) {

        if (!"ADMIN".equals(extractRole(jwt))) {
            return ResponseEntity.status(403).build();
        }

        List<Employee> employees =
                employeeService.getEmployeesForExport(status, quickFilter, fromDate, toDate);

        ByteArrayInputStream in =
                excelService.exportToExcel(employees);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=employees.xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(in));
    }

    /* =========================
       IMPORT (NO KEYCLOAK)
       ========================= */
    @PostMapping("/import")
    public ResponseEntity<String> importEmployees(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal Jwt jwt) {

        if (!"ADMIN".equals(extractRole(jwt))) {
            return ResponseEntity.status(403).body("Only ADMIN can import");
        }

        try {
            List<Employee> employees =
                    excelService.importFromExcel(
                            file,
                            departmentRepository,
                            roleRepository
                    );

            for (Employee emp : employees) {
                employeeService.save(emp);
            }

            return ResponseEntity.ok("Employees imported successfully");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}