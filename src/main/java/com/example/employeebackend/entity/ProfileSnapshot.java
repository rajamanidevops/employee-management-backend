package com.example.employeebackend.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // ✅

@Entity
@Table(name = "profile_snapshot")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProfileSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id", nullable = false, unique = true)
    @JsonIgnore
    private Profile profile;

    // ===== BASIC INFORMATION =====
    private String prefix;
    private String firstName;
    private String middleName;
    private String lastName;
    private String employeeCode;
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String nationality;
    private String workEmail;
    private String isdCode;
    private String mobileNumber;
    private String biometricId;
    private String skillType;

    // ===== EMPLOYMENT STATUS & TYPE =====
    private String employmentType;
    private String employmentStatus;
    private LocalDate dateOfJoining;
    private LocalDate dateOfConfirmation;
    private LocalDate retirementDate;
    private String otherStatusId;
    private LocalDate otherStatusDate;
    private String otherStatusRemarks;

    // ===== POSITION DETAILS =====
    private String company;
    private String businessUnit;
    private String department;
    private String subDepartment;
    private String designation;
    private String region;
    private String branch;
    private String subBranch;
    private String grade;
    private String reportingManager;
    private String functionalManager;

    // ===== GETTERS & SETTERS =====
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Profile getProfile() { return profile; }
    public void setProfile(Profile profile) { this.profile = profile; }

    public String getPrefix() { return prefix; }
    public void setPrefix(String prefix) { this.prefix = prefix; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getWorkEmail() { return workEmail; }
    public void setWorkEmail(String workEmail) { this.workEmail = workEmail; }

    public String getIsdCode() { return isdCode; }
    public void setIsdCode(String isdCode) { this.isdCode = isdCode; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getBiometricId() { return biometricId; }
    public void setBiometricId(String biometricId) { this.biometricId = biometricId; }

    public String getSkillType() { return skillType; }
    public void setSkillType(String skillType) { this.skillType = skillType; }

    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }

    public String getEmploymentStatus() { return employmentStatus; }
    public void setEmploymentStatus(String employmentStatus) { this.employmentStatus = employmentStatus; }

    public LocalDate getDateOfJoining() { return dateOfJoining; }
    public void setDateOfJoining(LocalDate dateOfJoining) { this.dateOfJoining = dateOfJoining; }

    public LocalDate getDateOfConfirmation() { return dateOfConfirmation; }
    public void setDateOfConfirmation(LocalDate dateOfConfirmation) { this.dateOfConfirmation = dateOfConfirmation; }

    public LocalDate getRetirementDate() { return retirementDate; }
    public void setRetirementDate(LocalDate retirementDate) { this.retirementDate = retirementDate; }

    public String getOtherStatusId() { return otherStatusId; }
    public void setOtherStatusId(String otherStatusId) { this.otherStatusId = otherStatusId; }

    public LocalDate getOtherStatusDate() { return otherStatusDate; }
    public void setOtherStatusDate(LocalDate otherStatusDate) { this.otherStatusDate = otherStatusDate; }

    public String getOtherStatusRemarks() { return otherStatusRemarks; }
    public void setOtherStatusRemarks(String otherStatusRemarks) { this.otherStatusRemarks = otherStatusRemarks; }

    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }

    public String getBusinessUnit() { return businessUnit; }
    public void setBusinessUnit(String businessUnit) { this.businessUnit = businessUnit; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getSubDepartment() { return subDepartment; }
    public void setSubDepartment(String subDepartment) { this.subDepartment = subDepartment; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public String getSubBranch() { return subBranch; }
    public void setSubBranch(String subBranch) { this.subBranch = subBranch; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getReportingManager() { return reportingManager; }
    public void setReportingManager(String reportingManager) { this.reportingManager = reportingManager; }

    public String getFunctionalManager() { return functionalManager; }
    public void setFunctionalManager(String functionalManager) { this.functionalManager = functionalManager; }
}