package com.example.employeebackend.launcher;

import java.io.*;

public class AiLauncher {

    public static void main(String[] args) throws Exception {

        System.out.println("🤖 AI Assistant launching Spring Boot...\n");

        // ✅ Use Maven Wrapper (no admin rights needed)
        ProcessBuilder pb = new ProcessBuilder(
                "cmd", "/c", "mvnw.cmd", "spring-boot:run"
        );

        pb.redirectErrorStream(true);
        pb.directory(new File(".")); // project root
        Process process = pb.start();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        StringBuilder errorBuffer = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            System.out.println(line);

            // Capture error lines
            if (line.toLowerCase().contains("error")
                    || line.toLowerCase().contains("exception")
                    || line.toLowerCase().contains("failed")) {
                errorBuffer.append(line).append("\n");
            }

            // Detect successful startup
            if (line.contains("Started EmployeeBackendApplication")) {
                System.out.println("\n✅ Application started successfully!");
                break; // stop reading errors
            }
        }

        // Show AI error analysis if errors detected
        if (errorBuffer.length() > 0) {
            System.out.println("\n🤖 AI Assistant Analysis:\n");
            System.out.println(ErrorAnalyzer.analyze(errorBuffer.toString()));
        }

        process.waitFor();
    }
}