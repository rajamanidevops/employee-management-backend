/*package com.example.employeebackend.chatbot.service;
import com.example.employeebackend.entity.Department;
import com.example.employeebackend.entity.*;
import com.example.employeebackend.repository.*;
import org.springframework.stereotype.Service;
import com.example.employeebackend.chatbot.repository.ChatbotRepository;
import com.example.employeebackend.entity.Chatbot;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatbotService {

    private final ProfileRepository profileRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private final ProfileBankRepository profileBankRepository;
    private final ProfileDocumentsRepository profileDocumentsRepository;
    private final ProfileEducationRepository profileEducationRepository;
    private final ProfileFamilyRepository profileFamilyRepository;
    private final ProfileHistoryRepository profileHistoryRepository;
    private final ProfilePersonalRepository profilePersonalRepository;
    private final ProfileProfessionalRepository profileProfessionalRepository;
    private final ProfileSnapshotRepository profileSnapshotRepository;
    private final ChatbotRepository chatbotRepository;

    public ChatbotService(
            ProfileRepository profileRepository,
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository,
            RoleRepository roleRepository,
            ProfileBankRepository profileBankRepository,
            ProfileDocumentsRepository profileDocumentsRepository,
            ProfileEducationRepository profileEducationRepository,
            ProfileFamilyRepository profileFamilyRepository,
            ProfileHistoryRepository profileHistoryRepository,
            ProfilePersonalRepository profilePersonalRepository,
            ProfileProfessionalRepository profileProfessionalRepository,
            ProfileSnapshotRepository profileSnapshotRepository,
            ChatbotRepository chatbotRepository
    ) {
        this.profileRepository = profileRepository;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
        this.profileBankRepository = profileBankRepository;
        this.profileDocumentsRepository = profileDocumentsRepository;
        this.profileEducationRepository = profileEducationRepository;
        this.profileFamilyRepository = profileFamilyRepository;
        this.profileHistoryRepository = profileHistoryRepository;
        this.profilePersonalRepository = profilePersonalRepository;
        this.profileProfessionalRepository = profileProfessionalRepository;
        this.profileSnapshotRepository = profileSnapshotRepository;
        this.chatbotRepository = chatbotRepository;
    }

    // ==================== MAIN ASK ====================
    public String ask(String role, String email, String question) {

        boolean isAdmin = "ADMIN".equalsIgnoreCase(role);
        String lower = question.toLowerCase().trim();

        try {
            // ===== GREETINGS =====
            if (lower.matches("hi|hello|hey|hai")) {
                return isAdmin
                        ? "Hello Admin 👋 How can I help you?"
                        : "Hello 👋 I am your Employee Assistant. How can I help you?";
            }

            // ===== TRAINED DATA (TOP PRIORITY) =====
            List<Chatbot> trained;

            if (isAdmin) {
                // ✅ Admin can access ADMIN + USER trained data
                trained = chatbotRepository
                        .findByQuestionContainingIgnoreCaseAndRoleIgnoreCase(question, "ADMIN");

                if (trained.isEmpty()) {
                    trained = chatbotRepository
                            .findByQuestionContainingIgnoreCaseAndRoleIgnoreCase(question, "USER");
                }
            } else {
                trained = chatbotRepository
                        .findByQuestionContainingIgnoreCaseAndRoleIgnoreCase(question, "USER");
            }

            if (!trained.isEmpty()) {
                return trained.get(0).getAnswer();
            }

            // ===== ADMIN FLOW =====
            if (isAdmin) {

                // ✅ ADMIN → OWN PROFILE FIRST
                if (lower.contains("my profile") ||
                        lower.contains("basic info") ||
                        lower.contains("snapshot")) {

                    return handleUserQuestions(email, question);
                }

                // ✅ ADMIN → OTHER EMPLOYEE PROFILE
                if (lower.contains("profile") ||
                lower.contains("basic info")) {
                    return handleAdminEmployeeProfile(question);
                }
                // ✅ Admin commands
                if (lower.contains("show employees") ||
                        lower.contains("all employees") ||
                        lower.contains("employee details") ||
                        lower.contains("total active employees") ||
                        lower.contains("total inactive employees") ||
                        lower.contains("all departments") ||
                        lower.contains("employees in department") ||
                        lower.contains("all roles") ||
                        lower.contains("status") ||
                        lower.equals("help")) {

                    return handleAdminQuestions(question);
                }
                // ===== USER HELP =====
                if (!isAdmin && lower.equals("help")) {
                    return "You can ask:\n" +
                            "- My profile\n" +
                            "- My basic information\n" +
                            "- Bank details\n" +
                            "- Documents\n" +
                            "- Education\n" +
                            "- Professional experience\n" +
                            "- Personal info\n" +
                            "- Family details\n" +
                            "- Profile history\n" +
                            "- Snapshot";
                }
                // ✅ IMPORTANT FIX:
                // If not admin command → fallback to USER questions
                return handleUserQuestions(email, question);
            }

            // ===== USER FLOW =====
            return handleUserQuestions(email, question);

        } catch (Exception e) {
            return analyzeError(e);
        }
    }
    private String formatDepartments(List<Department> list) {
        if (list == null || list.isEmpty()) {
            return "No departments found.";
        }

        StringBuilder sb = new StringBuilder();
        int i = 1;

        for (Department d : list) {
            sb.append(i++)
                    .append(". ")
                    .append(d.getName());

            if (d.getDescription() != null) {
                sb.append(" - ").append(d.getDescription());
            }

            sb.append("\n");
        }
        return sb.toString();
    }

    // ==================== USER ====================
    private String handleUserQuestions(String email, String question) {

        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        String q = question.toLowerCase();

        if (q.contains("profile") || q.contains("basic info") || q.contains("snapshot")) {
            return formatBasicProfile(profile);
        }

        if (q.contains("bank")) {
            return profileBankRepository.findByProfile(profile)
                    .map(this::formatBankDetails)
                    .orElse("No bank details found.");
        }

        if (q.contains("documents")) {
            return profileDocumentsRepository.findByProfile(profile)
                    .map(this::formatDocuments)
                    .orElse("No documents found.");
        }

        if (q.contains("education")) {
            List<ProfileEducation> list = profileEducationRepository.findByProfile(profile);
            return list.isEmpty() ? "No education records found." : formatEducationList(list);
        }

        if (q.contains("professional")) {
            List<ProfileProfessional> list = profileProfessionalRepository.findByProfile(profile);
            return list.isEmpty() ? "No professional experience found." : formatProfessionalList(list);
        }

        if (q.contains("personal")) {
            return profilePersonalRepository.findByProfile(profile)
                    .map(this::formatPersonal)
                    .orElse("No personal info found.");
        }

        if (q.contains("family")) {
            return profileFamilyRepository.findByProfile(profile)
                    .map(this::formatFamily)
                    .orElse("No family info found.");
        }

        if (q.contains("history")) {
            List<ProfileHistory> list = profileHistoryRepository.findByProfile(profile);
            return list.isEmpty() ? "No history found." : formatHistoryList(list);
        }

        return "Sorry, I can only answer questions about your profile.";
    }

    // ==================== ADMIN ====================
    private String handleAdminQuestions(String question) {

        String lower = question.toLowerCase().trim();

        // 1️⃣ Show all employees
        if (lower.contains("show employees") || lower.contains("all employees")) {
            return formatEmployeeList(employeeRepository.findAll());
        }

        // 2️⃣ Employee details by email
        if (lower.contains("employee details for email")) {
            String email = question.substring(lower.indexOf("email") + 5).trim();
            return employeeRepository.findByEmail(email)
                    .map(this::formatEmployeeDetails)
                    .orElse("Employee not found.");
        }

        // 3️⃣ Show <name> profile
        if (lower.startsWith("show") && lower.contains("profile")) {
            String name = question
                    .replaceAll("(?i)show", "")
                    .replaceAll("(?i)profile", "")
                    .trim();

            return employeeRepository.findAll().stream()
                    .filter(e -> e.getName().equalsIgnoreCase(name))
                    .findFirst()
                    .map(this::formatEmployeeDetails)
                    .orElse("Employee not found.");
        }

        // 4️⃣ <name> status  (example: nicky status)
        if (lower.endsWith("status")) {
            String name = question.replaceAll("(?i)status", "").trim();

            return employeeRepository.findAll().stream()
                    .filter(e -> e.getName().equalsIgnoreCase(name))
                    .findFirst()
                    .map(e -> e.getName() + " is " +
                            (Boolean.TRUE.equals(e.getActive()) ? "Active" : "Inactive"))
                    .orElse("Employee not found.");
        }

        // 5️⃣ Total active employees
        if (lower.contains("total active employees")) {
            return "Total active employees: " + employeeRepository.countByActiveTrue();
        }

        // 6️⃣ Total inactive employees
        if (lower.contains("total inactive employees")) {
            return "Total inactive employees: " + employeeRepository.countByActiveFalse();
        }

        // 7️⃣ All departments
        if (lower.contains("all departments")) {
            return formatDepartments(departmentRepository.findAll());
        }

        // 8️⃣ All roles
        if (lower.contains("all roles")) {
            return roleRepository.findAll()
                    .stream()
                    .map(Role::getRole)
                    .collect(Collectors.joining(", "));
        }

        // 9️⃣ Help
        if (lower.equals("help")) {
            return """
                Admin commands:
                - Show employees
                - Show <name> profile
                - <name> status
                - Employee details for email <email>
                - Total active employees
                - Total inactive employees
                - All departments
                - All roles
                """;
        }

        return "Sorry, I do not understand this admin query.";
    }
    // ===== ADMIN → EMPLOYEE PROFILE =====
    private String handleAdminEmployeeProfile(String question) {

        String name = question.toLowerCase()
                .replace("show", "")
                .replace("profile", "")
                .trim();

        Optional<Employee> empOpt = employeeRepository.findAll()
                .stream()
                .filter(e -> e.getName() != null && e.getName().equalsIgnoreCase(name))
                .findFirst();

        if (empOpt.isEmpty()) return "Employee not found.";

        Employee e = empOpt.get();

        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(e.getName()).append("\n")
                .append("Email: ").append(e.getEmail()).append("\n")
                .append("Status: ")
                .append(Boolean.TRUE.equals(e.getActive()) ? "Active" : "Inactive")
                .append("\n");

        profileRepository.findByEmail(e.getEmail())
                .ifPresent(p -> sb.append(formatBasicProfile(p)));

        return sb.toString();
    }

    // ==================== FORMATTERS (UNCHANGED) ====================
    private String formatBasicProfile(Profile p) {
        return String.format(
                "Name: %s\nEmail: %s\nPhone: %s\nAddress: %s\nDesignation: %s",
                p.getName(), p.getEmail(), p.getPhone(), p.getAddress(), p.getDesignation()
        );
    }

    private String formatBankDetails(ProfileBank b) {
        return "Bank Name: " + b.getBankName() +
                "\nAccount Number: " + b.getAccountNumber() +
                "\nIFSC: " + b.getIfscCode();
    }

    private String formatDocuments(ProfileDocuments d) {
        return "Aadhaar: " + d.getAadhaar() +
                "\nPAN: " + d.getPan();
    }

    private String formatEducationList(List<ProfileEducation> list) {
        return list.stream()
                .map(e -> e.getDegree() + " - " + e.getInstitute())
                .collect(Collectors.joining("\n"));
    }

    private String formatProfessionalList(List<ProfileProfessional> list) {
        return list.stream()
                .map(p -> p.getCompanyName() + " (" + p.getDesignation() + ")")
                .collect(Collectors.joining("\n"));
    }

    private String formatPersonal(ProfilePersonal p) {
        return "Personal Email: " + p.getPersonalEmail();
    }

    private String formatFamily(ProfileFamily f) {
        return "Father: " + f.getFatherName() + "\nMother: " + f.getMotherName();
    }

    private String formatHistoryList(List<ProfileHistory> list) {
        return list.stream()
                .map(h -> h.getType() + ": " + h.getOldValue() + " → " + h.getNewValue())
                .collect(Collectors.joining("\n"));
    }

    private String formatEmployeeList(List<Employee> list) {
        if (list.isEmpty()) return "No employees found.";
        return list.stream()
                .map(e -> e.getName() + " | " + e.getEmail())
                .collect(Collectors.joining("\n"));
    }

    private String formatEmployeeDetails(Employee e) {
        return "Name: " + e.getName() +
                "\nEmail: " + e.getEmail() +
                "\nEmployee No: " + e.getEmpNo();
    }
    // ===================== WRAPPERS =====================

    public String getAnswer(String role, String email, String question) {
        return ask(role, email, question);
    }

    public String train(String role, String email, String question, String answer) {
        // later you can save into DB
        return "Training saved for question: [" + question + "] with answer: [" + answer + "]";
    }
    private String analyzeError(Exception e) {

        String msg = e.getMessage();

        if (msg == null) {
            return "❌ Unknown error occurred. Please check backend logs.";
        }

        if (msg.contains("Profile not found")) {
            return """
        ❌ Profile not found

        ✔ Possible reasons:
        - User email not present in database
        - User not registered

        ✔ Solution:
        - Ask admin to create profile
        """;
        }

        if (msg.contains("Access Denied")) {
            return """
        ❌ Access denied

        ✔ Possible reasons:
        - Role mismatch
        - Token expired

        ✔ Solution:
        - Login again
        """;
        }

        if (msg.contains("Communications link failure")) {
            return """
        ❌ Database connection failed

        ✔ Solution:
        - Start MySQL server
        - Check DB URL / username / password
        """;
        }

        return "❌ Backend Error: " + msg;
    }
}*/
package com.example.employeebackend.chatbot.service;

import com.example.employeebackend.entity.*;
import com.example.employeebackend.repository.*;
import com.example.employeebackend.chatbot.repository.ChatbotRepository;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatbotService {

    private final ProfileRepository profileRepository;
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final ProfileBankRepository profileBankRepository;
    private final ProfileDocumentsRepository profileDocumentsRepository;
    private final ProfileEducationRepository profileEducationRepository;
    private final ProfileFamilyRepository profileFamilyRepository;
    private final ProfileHistoryRepository profileHistoryRepository;
    private final ProfilePersonalRepository profilePersonalRepository;
    private final ProfileProfessionalRepository profileProfessionalRepository;
    private final ProfileSnapshotRepository profileSnapshotRepository;
    private final ChatbotRepository chatbotRepository;

    public ChatbotService(
            ProfileRepository profileRepository,
            EmployeeRepository employeeRepository,
            DepartmentRepository departmentRepository,
            ProfileBankRepository profileBankRepository,
            ProfileDocumentsRepository profileDocumentsRepository,
            ProfileEducationRepository profileEducationRepository,
            ProfileFamilyRepository profileFamilyRepository,
            ProfileHistoryRepository profileHistoryRepository,
            ProfilePersonalRepository profilePersonalRepository,
            ProfileProfessionalRepository profileProfessionalRepository,
            ProfileSnapshotRepository profileSnapshotRepository,
            ChatbotRepository chatbotRepository
    ) {
        this.profileRepository = profileRepository;
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.profileBankRepository = profileBankRepository;
        this.profileDocumentsRepository = profileDocumentsRepository;
        this.profileEducationRepository = profileEducationRepository;
        this.profileFamilyRepository = profileFamilyRepository;
        this.profileHistoryRepository = profileHistoryRepository;
        this.profilePersonalRepository = profilePersonalRepository;
        this.profileProfessionalRepository = profileProfessionalRepository;
        this.profileSnapshotRepository = profileSnapshotRepository;
        this.chatbotRepository = chatbotRepository;
    }

    // ==================== MAIN ASK ====================
    public String ask(String role, String email, String question) {

        boolean isAdmin = "ADMIN".equalsIgnoreCase(role);
        String lower = question.toLowerCase().trim();

        try {
            // ===== GREETING =====
            if (lower.matches("hi|hello|hey|hai")) {
                return isAdmin
                        ? "Hello Admin 👋 How can I help you?"
                        : "Hello 👋 I am your Employee Assistant. How can I help you?";
            }

            // ===== TRAINED DATA =====
            List<Chatbot> trained;

            if (isAdmin) {
                trained = chatbotRepository
                        .findByQuestionContainingIgnoreCaseAndRoleIgnoreCase(question, "ADMIN");

                if (trained.isEmpty()) {
                    trained = chatbotRepository
                            .findByQuestionContainingIgnoreCaseAndRoleIgnoreCase(question, "USER");
                }
            } else {
                trained = chatbotRepository
                        .findByQuestionContainingIgnoreCaseAndRoleIgnoreCase(question, "USER");
            }

            if (!trained.isEmpty()) {
                return trained.get(0).getAnswer();
            }

            // ===== ADMIN FLOW =====
            if (isAdmin) {

                if (lower.contains("my profile") ||
                        lower.contains("basic info") ||
                        lower.contains("snapshot")) {
                    return handleUserQuestions(email, question);
                }

                if (lower.contains("profile")) {
                    return handleAdminEmployeeProfile(question);
                }

                return handleAdminQuestions(question);
            }

            // ===== USER FLOW =====
            return handleUserQuestions(email, question);

        } catch (Exception e) {
            return analyzeError(e);
        }
    }

    // ==================== USER ====================
    private String handleUserQuestions(String email, String question) {

        Profile profile = profileRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Profile not found"));

        String q = question.toLowerCase();

        if (q.contains("profile") || q.contains("basic info") || q.contains("snapshot")) {
            return formatBasicProfile(profile);
        }

        if (q.contains("bank")) {
            return profileBankRepository.findByProfile(profile)
                    .map(this::formatBankDetails)
                    .orElse("No bank details found.");
        }

        if (q.contains("documents")) {
            return profileDocumentsRepository.findByProfile(profile)
                    .map(this::formatDocuments)
                    .orElse("No documents found.");
        }

        if (q.contains("education")) {
            List<ProfileEducation> list = profileEducationRepository.findByProfile(profile);
            return list.isEmpty() ? "No education records found." : formatEducationList(list);
        }

        if (q.contains("professional")) {
            List<ProfileProfessional> list = profileProfessionalRepository.findByProfile(profile);
            return list.isEmpty() ? "No professional experience found." : formatProfessionalList(list);
        }

        if (q.contains("family")) {
            return profileFamilyRepository.findByProfile(profile)
                    .map(this::formatFamily)
                    .orElse("No family info found.");
        }

        return "Sorry, I can only answer questions about your profile.";
    }

    // ==================== ADMIN ====================
    private String handleAdminQuestions(String question) {

        String lower = question.toLowerCase();

        if (lower.contains("show employees")) {
            return employeeRepository.findAll().stream()
                    .map(e -> e.getName() + " | " + e.getEmail())
                    .collect(Collectors.joining("\n"));
        }

        if (lower.contains("all departments")) {
            return departmentRepository.findAll().stream()
                    .map(d -> d.getName())
                    .collect(Collectors.joining(", "));
        }

        /*if (lower.contains("total active employees")) {
            return "Total active employees: " + employeeRepository.countByActiveTrueAndDeletedFalse();
        }

        if (lower.contains("total inactive employees")) {
            return "Total inactive employees: " + employeeRepository.countByActiveFalseAndDeletedFalse();
        }*/
        if (lower.contains("total active employees")) {
            return "Total active employees: "
                    + employeeRepository.countByStatus(EmployeeStatus.ACTIVE);
        }

        if (lower.contains("total inactive employees")) {
            return "Total inactive employees: "
                    + employeeRepository.countByStatus(EmployeeStatus.INACTIVE);
        }

        return "Sorry, I do not understand this admin query.";
    }

    // ==================== ADMIN → EMPLOYEE PROFILE ====================
    private String handleAdminEmployeeProfile(String question) {

        String name = question.replace("show", "")
                .replace("profile", "")
                .trim();

        Optional<Employee> empOpt = employeeRepository.findAll().stream()
                .filter(e -> e.getName().equalsIgnoreCase(name))
                .findFirst();

        if (empOpt.isEmpty()) return "Employee not found.";

        Employee e = empOpt.get();

        return "Name: " + e.getName() +
                "\nEmail: " + e.getEmail() +
                "\nStatus: " + (e.getStatus() != null ? e.getStatus().name() : "UNKNOWN");
    }

    // ==================== FORMATTERS ====================
    private String formatBasicProfile(Profile p) {
        return "Name: " + p.getName() +
                "\nEmail: " + p.getEmail() +
                "\nPhone: " + p.getPhone();
    }

    private String formatBankDetails(ProfileBank b) {
        return "Bank: " + b.getBankName() +
                "\nAccount: " + b.getAccountNumber();
    }

    private String formatDocuments(ProfileDocuments d) {
        return "Aadhaar: " + d.getAadhaar() +
                "\nPAN: " + d.getPan();
    }

    private String formatEducationList(List<ProfileEducation> list) {
        return list.stream()
                .map(e -> e.getDegree() + " - " + e.getInstitute())
                .collect(Collectors.joining("\n"));
    }

    private String formatProfessionalList(List<ProfileProfessional> list) {
        return list.stream()
                .map(p -> p.getCompanyName() + " (" + p.getDesignation() + ")")
                .collect(Collectors.joining("\n"));
    }

    private String formatFamily(ProfileFamily f) {
        return "Father: " + f.getFatherName();
    }

    private String analyzeError(Exception e) {
        return "❌ Backend Error: " + e.getMessage();
    }

    public String getAnswer(String role, String email, String question) {
        return ask(role, email, question);
    }
    public String train(String role, String email, String question, String answer) {
        Chatbot chatbot = new Chatbot();
        chatbot.setQuestion(question);
        chatbot.setAnswer(answer);
        chatbot.setRole(role);   // ADMIN / USER
        chatbot.setModule("GENERAL");

        chatbotRepository.save(chatbot);

        return "✅ Training saved successfully";
    }
}