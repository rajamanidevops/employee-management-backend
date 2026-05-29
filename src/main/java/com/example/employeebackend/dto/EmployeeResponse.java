/*package com.example.employeebackend.dto;

import com.example.employeebackend.entity.Employee;
import java.time.LocalDateTime;

public class EmployeeResponse {
    private Long id;
    private String empNo;
    private String name;
    private String email;
    private String departmentName;
    private String roleName;
    private Boolean active;       // ✅ Add this
    private LocalDateTime createdAt; // ✅ Add this if needed

    public EmployeeResponse(Employee emp) {
        this.id = emp.getId();
        this.empNo = emp.getEmpNo();
        this.name = emp.getName();
        this.email = emp.getEmail();
        this.departmentName = emp.getDepartment() != null ? emp.getDepartment().getName() : null;
        this.roleName = emp.getRole() != null ? emp.getRole().getRole() : null;
        this.active = emp.getActive();          // ✅ FIX: include active status
        this.createdAt = emp.getCreatedAt();    // ✅ optional
    }

    // Getters
    public Long getId() { return id; }
    public String getEmpNo() { return empNo; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getDepartmentName() { return departmentName; }
    public String getRoleName() { return roleName; }
    public Boolean getActive() { return active; }   // ✅ getter
    public LocalDateTime getCreatedAt() { return createdAt; }
}*/
package com.example.employeebackend.dto;

import com.example.employeebackend.entity.Employee;
import java.time.LocalDate;
import com.example.employeebackend.entity.EmployeeStatus;
public class EmployeeResponse {

    private Long id;
    private String empNo;
    private String name;
    private String email;
    private EmployeeStatus status;
    private String departmentName;
    private String role;
    private String managerEmpNo;
    private String managerName;
    // ✅ ADD THIS
    private LocalDate dateOfJoining;
    private LocalDate dateOfLeaving;

    public EmployeeResponse(Employee e) {
        this.id = e.getId();
        this.empNo = e.getEmpNo();
        this.name = e.getName();
        this.email = e.getEmail();
        this.status = e.getStatus();

        this.managerName = e.getManager() != null ? e.getManager().getName() : null;
        this.dateOfJoining = e.getDateOfJoining();
        this.dateOfLeaving = e.getDateOfLeaving();// ✅ IMPORTANT FIX

        if (e.getDepartment() != null) {
            this.departmentName = e.getDepartment().getName();
        }

        if (e.getRole() != null) {
            this.role = e.getRole().getRole();
        }


        if (e.getManager() != null) {
            this.managerEmpNo = e.getManager().getEmpNo();
            this.managerName = e.getManager().getName();
        }

    }

    public Long getId() { return id; }
    public String getEmpNo() { return empNo; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public EmployeeStatus getStatus() {
        return status;
    }
    public String getDepartmentName() { return departmentName; }
    public String getRole() { return role; }

    public String getManagerEmpNo() { return managerEmpNo; }
    public String getManagerName() { return managerName; }
    // ✅ ADD GETTER
    public LocalDate getDateOfJoining() { return dateOfJoining; }

    public LocalDate getDateOfLeaving() {
        return dateOfLeaving;
    }
}