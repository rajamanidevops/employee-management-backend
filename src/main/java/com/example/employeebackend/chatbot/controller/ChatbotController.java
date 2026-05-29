package com.example.employeebackend.chatbot.controller;

import com.example.employeebackend.chatbot.dto.ChatbotRequest;
import com.example.employeebackend.chatbot.dto.ChatbotResponse;
import com.example.employeebackend.chatbot.service.ChatbotService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.jdbc.core.JdbcTemplate;
@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin(origins = "http://localhost:4200")
public class ChatbotController {

    private final ChatbotService chatbotService;
    private final JdbcTemplate jdbcTemplate; // add this for DB health check

    public ChatbotController(ChatbotService chatbotService, JdbcTemplate jdbcTemplate) {
        this.chatbotService = chatbotService;
        this.jdbcTemplate = jdbcTemplate;
    }

    // ===================== ASK ENDPOINT =====================
    @PostMapping("/ask")
    public ChatbotResponse askQuestion(@RequestBody ChatbotRequest request) {
        String answer = chatbotService.getAnswer(
                request.getRole(),
                request.getEmail(),
                request.getQuestion()
        );
        return new ChatbotResponse(answer);
    }

    @PostMapping("/train")
    public ResponseEntity<ChatbotResponse> trainChatbot(@RequestBody ChatbotRequest request) {
        String response = chatbotService.train(
                request.getRole(),
                request.getEmail(),
                request.getQuestion(),
                request.getAnswer()
        );
        return ResponseEntity.ok(new ChatbotResponse(response));
    }

    // ===================== HEALTH CHECK =====================
    @GetMapping("/ask/health")
    public ResponseEntity<String> healthCheck() {
        try {
            // simple DB check
            jdbcTemplate.execute("SELECT 1");
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❌ DB or Backend error: " + e.getMessage());
        }
    }
}