package com.example.employeebackend.chatbot.dto;

public class ChatbotRequest {

    private String email;      // user email
    private String role;       // USER / ADMIN
    private String question;   // the question to ask
    private String answer;     // training answer (optional for /ask)

    // ======= GETTERS & SETTERS =======

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}