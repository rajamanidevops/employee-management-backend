package com.example.employeebackend.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // ✅
@Entity
@Table(name = "profile_bank")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProfileBank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔑 Link to Profile (NOT email)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id", nullable = false, unique = true)
    @JsonIgnore
    private Profile profile;

    // ===== BANK DETAILS =====
    private String bankName;
    private String accountHolderName;
    private String accountNumber;
    private String ifscCode;

    // ===== PAYMENT MODES =====
    private String salaryPayMode;
    private Boolean reimbursementSameAsSalary;
    private String reimbursementPayMode;

    // ===== GETTERS =====
    public Long getId() { return id; }
    public Profile getProfile() { return profile; }

    public String getBankName() { return bankName; }
    public String getAccountHolderName() { return accountHolderName; }
    public String getAccountNumber() { return accountNumber; }
    public String getIfscCode() { return ifscCode; }

    public String getSalaryPayMode() { return salaryPayMode; }
    public Boolean getReimbursementSameAsSalary() { return reimbursementSameAsSalary; }
    public String getReimbursementPayMode() { return reimbursementPayMode; }

    // ===== SETTERS =====
    public void setId(Long id) { this.id = id; }
    public void setProfile(Profile profile) { this.profile = profile; }

    public void setBankName(String bankName) { this.bankName = bankName; }
    public void setAccountHolderName(String accountHolderName) { this.accountHolderName = accountHolderName; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setIfscCode(String ifscCode) { this.ifscCode = ifscCode; }

    public void setSalaryPayMode(String salaryPayMode) { this.salaryPayMode = salaryPayMode; }
    public void setReimbursementSameAsSalary(Boolean reimbursementSameAsSalary) {
        this.reimbursementSameAsSalary = reimbursementSameAsSalary;
    }
    public void setReimbursementPayMode(String reimbursementPayMode) {
        this.reimbursementPayMode = reimbursementPayMode;
    }
}