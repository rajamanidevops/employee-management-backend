package com.example.employeebackend.dto;

import com.example.employeebackend.entity.Employee;
import com.example.employeebackend.entity.EmployeeStatus;

public class TeamMemberResponse {

    private String empNo;
    private String name;
    private String email;
    private String department;
    private EmployeeStatus status;

    public TeamMemberResponse(Employee emp) {
        this.empNo = emp.getEmpNo();
        this.name = emp.getName();
        this.email = emp.getEmail();
        this.department = emp.getDepartment() != null
                ? emp.getDepartment().getName()
                : null;
        this.status = emp.getStatus();
    }

    public String getEmpNo() {
        return empNo;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public EmployeeStatus getStatus() {
        return status;
    }
}