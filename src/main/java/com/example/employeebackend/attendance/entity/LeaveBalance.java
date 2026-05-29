package com.example.employeebackend.attendance.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "leave_balance")
public class LeaveBalance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private double clBalance;
    private double slBalance;
    private double elBalance;

    // getters & setters
    public Long getId() { return id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public double getClBalance() { return clBalance; }
    public void setClBalance(double clBalance) { this.clBalance = clBalance; }

    public double getSlBalance() { return slBalance; }
    public void setSlBalance(double slBalance) { this.slBalance = slBalance; }

    public double getElBalance() { return elBalance; }
    public void setElBalance(double elBalance) { this.elBalance = elBalance; }
}