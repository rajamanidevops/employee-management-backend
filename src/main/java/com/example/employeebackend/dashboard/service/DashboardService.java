package com.example.employeebackend.dashboard.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.example.employeebackend.entity.EmployeeStatus;
import org.springframework.stereotype.Service;

import com.example.employeebackend.dashboard.dto.DashboardDTO;
import com.example.employeebackend.entity.Employee;
import com.example.employeebackend.entity.Profile;
import com.example.employeebackend.repository.EmployeeRepository;
import com.example.employeebackend.repository.DepartmentRepository;
import com.example.employeebackend.repository.ProfileRepository;
import com.example.employeebackend.repository.ProfilePersonalRepository;
import com.example.employeebackend.repository.ProfileProfessionalRepository;
import com.example.employeebackend.repository.ProfileEducationRepository;
import com.example.employeebackend.repository.ProfileFamilyRepository;
import com.example.employeebackend.repository.ProfileHistoryRepository;
import com.example.employeebackend.repository.ProfileDocumentsRepository;
import com.example.employeebackend.repository.ProfileSnapshotRepository;
import com.example.employeebackend.repository.ProfileBankRepository;

@Service
public class DashboardService {

    private final EmployeeRepository employeeRepo;
    private final DepartmentRepository departmentRepo;
    private final ProfileRepository profileRepo;
    private final ProfilePersonalRepository personalRepo;
    private final ProfileProfessionalRepository professionalRepo;
    private final ProfileEducationRepository educationRepo;
    private final ProfileFamilyRepository familyRepo;
    private final ProfileHistoryRepository historyRepo;
    private final ProfileDocumentsRepository documentsRepo;
    private final ProfileSnapshotRepository snapshotRepo;
    private final ProfileBankRepository bankRepo;

    public DashboardService(
            EmployeeRepository employeeRepo,
            DepartmentRepository departmentRepo,
            ProfileRepository profileRepo,
            ProfilePersonalRepository personalRepo,
            ProfileProfessionalRepository professionalRepo,
            ProfileEducationRepository educationRepo,
            ProfileFamilyRepository familyRepo,
            ProfileHistoryRepository historyRepo,
            ProfileDocumentsRepository documentsRepo,
            ProfileSnapshotRepository snapshotRepo,
            ProfileBankRepository bankRepo) {

        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
        this.profileRepo = profileRepo;
        this.personalRepo = personalRepo;
        this.professionalRepo = professionalRepo;
        this.educationRepo = educationRepo;
        this.familyRepo = familyRepo;
        this.historyRepo = historyRepo;
        this.documentsRepo = documentsRepo;
        this.snapshotRepo = snapshotRepo;
        this.bankRepo = bankRepo;
    }

    // ✅ DASHBOARD KPI DATA
    public DashboardDTO getDashboardData() {
        DashboardDTO dto = new DashboardDTO();

        // ✅ TOTAL
        dto.setTotalEmployees(employeeRepo.count());

        dto.setActiveEmployees(employeeRepo.countCurrentlyActive());
        dto.setInactiveEmployees(employeeRepo.countCurrentlyInactive());
        dto.setTotalDepartments(departmentRepo.count());

        LocalDateTime start = LocalDate.now()
                .withDayOfMonth(1)
                .atStartOfDay();

        LocalDateTime end = start.plusMonths(1);

        // ✅ NEW THIS MONTH (ignore deleted)
        dto.setNewEmployeesThisMonth(
                employeeRepo.countByCreatedAtBetween(start, end)
        );

        dto.setAvgProfileCompletion(calculateAvgProfileCompletion());

        // ✅ DEPARTMENT COUNT (ignore deleted)
        Map<String, Long> deptMap = employeeRepo.findAll().stream()
                .filter(e -> e.getDepartment() != null)
                .filter(e -> e.getDateOfLeaving() == null
                        || e.getDateOfLeaving().isAfter(LocalDate.now()))
                .collect(Collectors.groupingBy(
                        e -> e.getDepartment().getName(),
                        Collectors.counting()
                ));

        dto.setEmployeesByDepartment(deptMap);

        return dto;
    }

    // ✅ PROFILE COMPLETION (EMAIL BASED)
    public Map<String, Boolean> getProfileCompletion(String email) {
        Map<String, Boolean> status = new HashMap<>();
        Profile profile = profileRepo.findByEmail(email).orElse(null);

        if (profile == null) return status;

        status.put("personal", personalRepo.findByProfile(profile).isPresent());
        status.put("professional", !professionalRepo.findByProfile(profile).isEmpty());
        status.put("education", !educationRepo.findByProfile(profile).isEmpty());
        status.put("family", familyRepo.findByProfile(profile).isPresent());
        status.put("history", !historyRepo.findByProfile(profile).isEmpty());
        status.put("documents", documentsRepo.findByProfile(profile).isPresent());
        status.put("snapshot", snapshotRepo.findByProfile(profile).isPresent());
        status.put("bank", bankRepo.findByProfile(profile).isPresent());

        return status;
    }

    // 🔥 HELPER: AVERAGE PROFILE COMPLETION %
    private double calculateAvgProfileCompletion() {

        var employees = employeeRepo.findAll();
        if (employees.isEmpty()) return 0;

        double totalCompletionRatio = 0;
        int profileCount = 0;

        for (Employee e : employees) {

            Profile profile = profileRepo.findByEmail(e.getEmail()).orElse(null);
            if (profile == null) continue; // 🔥 KEY FIX

            int completedTabs = 0;
            int totalTabs = 8;

            if (personalRepo.findByProfile(profile).isPresent()) completedTabs++;
            if (!professionalRepo.findByProfile(profile).isEmpty()) completedTabs++;
            if (!educationRepo.findByProfile(profile).isEmpty()) completedTabs++;
            if (familyRepo.findByProfile(profile).isPresent()) completedTabs++;
            if (!historyRepo.findByProfile(profile).isEmpty()) completedTabs++;
            if (documentsRepo.findByProfile(profile).isPresent()) completedTabs++;
            if (snapshotRepo.findByProfile(profile).isPresent()) completedTabs++;
            if (bankRepo.findByProfile(profile).isPresent()) completedTabs++;

            totalCompletionRatio += (double) completedTabs / totalTabs;
            profileCount++;
        }

        if (profileCount == 0) return 0;

        double avg = (totalCompletionRatio / profileCount) * 100;
        return Math.round(avg * 100.0) / 100.0;
    }
}