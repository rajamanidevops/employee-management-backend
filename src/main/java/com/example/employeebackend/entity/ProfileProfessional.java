package com.example.employeebackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "profile_professional")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProfileProfessional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many experiences → One profile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    @JsonIgnore
    private Profile profile;

    private Boolean fresher;
    private String companyName;
    private String designation;

    // ✅ IMPORTANT FIX
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fromDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate toDate;

    private Boolean currentlyWorking;

    // ===== GETTERS =====
    public Long getId() { return id; }
    public Profile getProfile() { return profile; }
    public Boolean getFresher() { return fresher; }
    public String getCompanyName() { return companyName; }
    public String getDesignation() { return designation; }
    public LocalDate getFromDate() { return fromDate; }
    public LocalDate getToDate() { return toDate; }
    public Boolean getCurrentlyWorking() { return currentlyWorking; }

    // ===== SETTERS =====
    public void setId(Long id) { this.id = id; }
    public void setProfile(Profile profile) { this.profile = profile; }
    public void setFresher(Boolean fresher) { this.fresher = fresher; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setDesignation(String designation) { this.designation = designation; }
    public void setFromDate(LocalDate fromDate) { this.fromDate = fromDate; }
    public void setToDate(LocalDate toDate) { this.toDate = toDate; }
    public void setCurrentlyWorking(Boolean currentlyWorking) {
        this.currentlyWorking = currentlyWorking;
    }
}