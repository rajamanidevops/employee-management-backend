package com.example.employeebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling   // ✅ REQUIRED
@SpringBootApplication
public class EmployeeBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeBackendApplication.class, args);
    }
}