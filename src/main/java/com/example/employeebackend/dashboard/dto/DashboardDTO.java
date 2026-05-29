package com.example.employeebackend.dashboard.dto;

import java.util.Map;

public class DashboardDTO {

    private Long totalEmployees;
    private Long activeEmployees;
    private Long inactiveEmployees;
    private Long newEmployeesThisMonth;
    private Double avgProfileCompletion;
    private Long totalDepartments;
    private Map<String, Long> employeesByDepartment;

    // Getters & Setters
    public Long getTotalEmployees() { return totalEmployees; }
    public void setTotalEmployees(Long totalEmployees) { this.totalEmployees = totalEmployees; }

    public Long getActiveEmployees() { return activeEmployees; }
    public void setActiveEmployees(Long activeEmployees) { this.activeEmployees = activeEmployees; }

    public Long getInactiveEmployees() { return inactiveEmployees; }
    public void setInactiveEmployees(Long inactiveEmployees) { this.inactiveEmployees = inactiveEmployees; }

    public Long getNewEmployeesThisMonth() { return newEmployeesThisMonth; }
    public void setNewEmployeesThisMonth(Long newEmployeesThisMonth) { this.newEmployeesThisMonth = newEmployeesThisMonth; }

    public Double getAvgProfileCompletion() { return avgProfileCompletion; }
    public void setAvgProfileCompletion(Double avgProfileCompletion) { this.avgProfileCompletion = avgProfileCompletion; }

    public Long getTotalDepartments() { return totalDepartments; }
    public void setTotalDepartments(Long totalDepartments) { this.totalDepartments = totalDepartments; }

    public Map<String, Long> getEmployeesByDepartment() { return employeesByDepartment; }
    public void setEmployeesByDepartment(Map<String, Long> employeesByDepartment) {
        this.employeesByDepartment = employeesByDepartment;
    }
}