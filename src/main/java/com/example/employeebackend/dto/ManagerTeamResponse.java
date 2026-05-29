package com.example.employeebackend.dto;

import java.util.List;

public class ManagerTeamResponse {

    private String managerEmpNo;
    private String managerName;
    private int teamSize;
    private List<TeamMemberResponse> team;

    public ManagerTeamResponse(
            String managerEmpNo,
            String managerName,
            List<TeamMemberResponse> team
    ) {
        this.managerEmpNo = managerEmpNo;
        this.managerName = managerName;
        this.team = team;
        this.teamSize = team.size();
    }

    public String getManagerEmpNo() {
        return managerEmpNo;
    }

    public String getManagerName() {
        return managerName;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public List<TeamMemberResponse> getTeam() {
        return team;
    }
}