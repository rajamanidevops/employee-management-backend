package com.example.employeebackend.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // ✅
@Entity
@Table(name = "profile_education")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProfileEducation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔑 Many education records → One profile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    @JsonIgnore
    private Profile profile;

    private String qualification;
    private String degree;
    private String specialization;
    private String institute;
    private String university;
    private Integer yearOfPassing;
    private String percentageOrCgpa;
    private Boolean highestQualification;

    // ===== GETTERS =====
    public Long getId() { return id; }
    public Profile getProfile() { return profile; }

    public String getQualification() { return qualification; }
    public String getDegree() { return degree; }
    public String getSpecialization() { return specialization; }
    public String getInstitute() { return institute; }
    public String getUniversity() { return university; }
    public Integer getYearOfPassing() { return yearOfPassing; }
    public String getPercentageOrCgpa() { return percentageOrCgpa; }
    public Boolean getHighestQualification() { return highestQualification; }

    // ===== SETTERS =====
    public void setId(Long id) { this.id = id; }
    public void setProfile(Profile profile) { this.profile = profile; }

    public void setQualification(String qualification) { this.qualification = qualification; }
    public void setDegree(String degree) { this.degree = degree; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public void setInstitute(String institute) { this.institute = institute; }
    public void setUniversity(String university) { this.university = university; }
    public void setYearOfPassing(Integer yearOfPassing) { this.yearOfPassing = yearOfPassing; }
    public void setPercentageOrCgpa(String percentageOrCgpa) { this.percentageOrCgpa = percentageOrCgpa; }
    public void setHighestQualification(Boolean highestQualification) {
        this.highestQualification = highestQualification;
    }
}