package com.example.employeebackend.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // ✅
@Entity
@Table(name = "profile_documents")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ProfileDocuments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Each profile has exactly one document record
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id", nullable = false, unique = true)
    @JsonIgnore
    private Profile profile;

    private String aadhaar;
    private String pan;
    private String resume;
    private String offerLetter;
    private String experienceLetter;

    // ===== GETTERS =====
    public Long getId() {
        return id;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getAadhaar() {
        return aadhaar;
    }

    public String getPan() {
        return pan;
    }

    public String getResume() {
        return resume;
    }

    public String getOfferLetter() {
        return offerLetter;
    }

    public String getExperienceLetter() {
        return experienceLetter;
    }

    // ===== SETTERS =====
    public void setId(Long id) {
        this.id = id;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setAadhaar(String aadhaar) {
        this.aadhaar = aadhaar;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public void setOfferLetter(String offerLetter) {
        this.offerLetter = offerLetter;
    }

    public void setExperienceLetter(String experienceLetter) {
        this.experienceLetter = experienceLetter;
    }
}