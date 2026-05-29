package com.example.employeebackend.dto;

import com.example.employeebackend.entity.*;
import java.util.List;

public class ProfileFullResponse {

    private Profile profile;
    private ProfileSnapshot snapshot;
    private ProfilePersonal personal;
    private ProfileFamily family;
    private ProfileBank bank;
    private ProfileDocuments documents;

    private List<ProfileEducation> education;
    private List<ProfileProfessional> professional;
    private List<ProfileHistory> history;

    // ===== GETTERS & SETTERS =====

    public Profile getProfile() { return profile; }
    public void setProfile(Profile profile) { this.profile = profile; }

    public ProfileSnapshot getSnapshot() { return snapshot; }
    public void setSnapshot(ProfileSnapshot snapshot) { this.snapshot = snapshot; }

    public ProfilePersonal getPersonal() { return personal; }
    public void setPersonal(ProfilePersonal personal) { this.personal = personal; }

    public ProfileFamily getFamily() { return family; }
    public void setFamily(ProfileFamily family) { this.family = family; }

    public ProfileBank getBank() { return bank; }
    public void setBank(ProfileBank bank) { this.bank = bank; }

    public ProfileDocuments getDocuments() { return documents; }
    public void setDocuments(ProfileDocuments documents) { this.documents = documents; }

    public List<ProfileEducation> getEducation() { return education; }
    public void setEducation(List<ProfileEducation> education) { this.education = education; }

    public List<ProfileProfessional> getProfessional() { return professional; }
    public void setProfessional(List<ProfileProfessional> professional) { this.professional = professional; }

    public List<ProfileHistory> getHistory() { return history; }
    public void setHistory(List<ProfileHistory> history) { this.history = history; }
}