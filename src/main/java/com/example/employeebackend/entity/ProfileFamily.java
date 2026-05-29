package com.example.employeebackend.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // ✅
@Entity
@Table(name = "profile_family")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProfileFamily {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ One Profile → One Family record
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id", nullable = false, unique = true)
    @JsonIgnore
    private Profile profile;

    // ===== PARENT DETAILS =====
    private String fatherName;
    private LocalDate fatherDob;

    private String motherName;
    private LocalDate motherDob;

    // ===== MARITAL DETAILS =====
    private String maritalStatus;

    // ===== GETTERS =====
    public Long getId() {
        return id;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getFatherName() {
        return fatherName;
    }

    public LocalDate getFatherDob() {
        return fatherDob;
    }

    public String getMotherName() {
        return motherName;
    }

    public LocalDate getMotherDob() {
        return motherDob;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    // ===== SETTERS =====
    public void setId(Long id) {
        this.id = id;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public void setFatherDob(LocalDate fatherDob) {
        this.fatherDob = fatherDob;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public void setMotherDob(LocalDate motherDob) {
        this.motherDob = motherDob;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }
}