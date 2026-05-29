package com.example.employeebackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "department")
public class Department {

    @Id
    @Column(name = "dept_id")
    private Long deptId;   // 👈 USER INPUT

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    @OneToMany(mappedBy = "department")
    @JsonIgnore
    private List<Employee> employees;

    // Constructors
    public Department() {}

    public Department(Long deptId, String name, String description) {
        this.deptId = deptId;
        this.name = name;
        this.description = description;
    }

    // Getters & Setters
    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}









