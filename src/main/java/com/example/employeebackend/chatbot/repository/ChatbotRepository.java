package com.example.employeebackend.chatbot.repository;

import com.example.employeebackend.entity.Chatbot;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatbotRepository extends JpaRepository<Chatbot, Long> {

    List<Chatbot> findByQuestionContainingIgnoreCaseAndRoleIgnoreCase(String question, String role);
}
