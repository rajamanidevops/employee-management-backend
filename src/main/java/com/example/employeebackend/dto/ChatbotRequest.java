package com.example.employeebackend.dto;

public class ChatbotRequest {
    private String question;
    private String role;
    private String module;
    private String answer; // add this
    private String email;  // add this

    // ================== GETTERS & SETTERS ==================
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }

    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}