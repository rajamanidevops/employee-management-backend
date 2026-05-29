/*package com.example.employeebackend.service;

import com.example.employeebackend.dto.ManagerTeamResponse;
import com.example.employeebackend.dto.TeamMemberResponse;
import com.example.employeebackend.entity.Employee;
import com.example.employeebackend.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ManagerService {

    private final EmployeeRepository employeeRepository;

    public ManagerService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // =========================
    // MANAGER: VIEW OWN TEAM
    // =========================
    public ManagerTeamResponse getMyTeam(String email) {

        Employee manager = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        List<Employee> team = employeeRepository.findByManager(manager);

        List<TeamMemberResponse> members = team.stream()
                .map(TeamMemberResponse::new)
                .toList();

        return new ManagerTeamResponse(
                manager.getEmpNo(),
                manager.getName(),
                members
        );
    }

    // =========================
    // CHECK IF USER IS MANAGER
    // =========================
    public boolean isManager(String email) {

        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return !employeeRepository.findByManager(employee).isEmpty();
    }

    // =========================
    // ADMIN: ASSIGN MANAGER (empNo)
    // =========================
    public void assignManager(String empNo, String managerEmpNo) {

        if (empNo.equals(managerEmpNo)) {
            throw new RuntimeException("Employee cannot be their own manager");
        }

        Employee employee = employeeRepository.findByEmpNo(empNo)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Employee manager = employeeRepository.findByEmpNo(managerEmpNo)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        if (!manager.getActive()) {
            throw new RuntimeException("Inactive employee cannot be a manager");
        }

        employee.setManager(manager);
        employeeRepository.save(employee);
    }

    // =========================
    // ADMIN: REMOVE MANAGER
    // =========================
    public void removeManager(String empNo) {

        Employee employee = employeeRepository.findByEmpNo(empNo)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.setManager(null);
        employeeRepository.save(employee);
    }

    // =========================
    // ADMIN: VIEW ANY MANAGER TEAM
    // =========================
    public ManagerTeamResponse getManagerTeam(String managerEmpNo) {

        Employee manager = employeeRepository.findByEmpNo(managerEmpNo)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        List<Employee> team = employeeRepository.findByManager(manager);

        List<TeamMemberResponse> members = team.stream()
                .map(TeamMemberResponse::new)
                .collect(Collectors.toList());

        return new ManagerTeamResponse(
                manager.getEmpNo(),
                manager.getName(),
                members
        );
    }
}*/