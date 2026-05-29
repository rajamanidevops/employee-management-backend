package com.example.employeebackend.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // ✅
@Entity
@Table(name = "profile_personal")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProfilePersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ One Profile → One Personal record
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id", nullable = false, unique = true)
    @JsonIgnore
    private Profile profile;

    // ===== CONTACT DETAILS =====
    private String personalEmail;

    private String emergencyContactName;
    private String emergencyIsdCode;
    private String emergencyContactNumber;
    private String emergencyRelation;

    // ===== PRESENT ADDRESS =====
    private String currentAddress;
    private String currentCountry;
    private String currentState;
    private String currentCity;
    private String currentPin;

    // ===== PERMANENT ADDRESS =====
    private Boolean sameAsPresent;

    private String permanentAddress;
    private String permanentCountry;
    private String permanentState;
    private String permanentCity;
    private String permanentPin;

    // ===== GETTERS =====
    public Long getId() { return id; }
    public Profile getProfile() { return profile; }

    public String getPersonalEmail() { return personalEmail; }

    public String getEmergencyContactName() { return emergencyContactName; }
    public String getEmergencyIsdCode() { return emergencyIsdCode; }
    public String getEmergencyContactNumber() { return emergencyContactNumber; }
    public String getEmergencyRelation() { return emergencyRelation; }

    public String getCurrentAddress() { return currentAddress; }
    public String getCurrentCountry() { return currentCountry; }
    public String getCurrentState() { return currentState; }
    public String getCurrentCity() { return currentCity; }
    public String getCurrentPin() { return currentPin; }

    public Boolean getSameAsPresent() { return sameAsPresent; }

    public String getPermanentAddress() { return permanentAddress; }
    public String getPermanentCountry() { return permanentCountry; }
    public String getPermanentState() { return permanentState; }
    public String getPermanentCity() { return permanentCity; }
    public String getPermanentPin() { return permanentPin; }

    // ===== SETTERS =====
    public void setId(Long id) { this.id = id; }
    public void setProfile(Profile profile) { this.profile = profile; }

    public void setPersonalEmail(String personalEmail) { this.personalEmail = personalEmail; }

    public void setEmergencyContactName(String emergencyContactName) { this.emergencyContactName = emergencyContactName; }
    public void setEmergencyIsdCode(String emergencyIsdCode) { this.emergencyIsdCode = emergencyIsdCode; }
    public void setEmergencyContactNumber(String emergencyContactNumber) { this.emergencyContactNumber = emergencyContactNumber; }
    public void setEmergencyRelation(String emergencyRelation) { this.emergencyRelation = emergencyRelation; }

    public void setCurrentAddress(String currentAddress) { this.currentAddress = currentAddress; }
    public void setCurrentCountry(String currentCountry) { this.currentCountry = currentCountry; }
    public void setCurrentState(String currentState) { this.currentState = currentState; }
    public void setCurrentCity(String currentCity) { this.currentCity = currentCity; }
    public void setCurrentPin(String currentPin) { this.currentPin = currentPin; }

    public void setSameAsPresent(Boolean sameAsPresent) { this.sameAsPresent = sameAsPresent; }

    public void setPermanentAddress(String permanentAddress) { this.permanentAddress = permanentAddress; }
    public void setPermanentCountry(String permanentCountry) { this.permanentCountry = permanentCountry; }
    public void setPermanentState(String permanentState) { this.permanentState = permanentState; }
    public void setPermanentCity(String permanentCity) { this.permanentCity = permanentCity; }
    public void setPermanentPin(String permanentPin) { this.permanentPin = permanentPin; }
}