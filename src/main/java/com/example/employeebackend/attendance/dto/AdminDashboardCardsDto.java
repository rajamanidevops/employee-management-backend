package com.example.employeebackend.attendance.dto;

public class AdminDashboardCardsDto {

    private long totalEmployees;
    private long totalDepartments;

    private long presentToday;
    private long absentToday;
    private long leaveToday;
    private long lateToday;
    private long halfDayToday;

    public AdminDashboardCardsDto(
            long totalEmployees,
            long totalDepartments,
            long presentToday,
            long absentToday,
            long leaveToday,
            long lateToday,
            long halfDayToday
    ) {
        this.totalEmployees = totalEmployees;
        this.totalDepartments = totalDepartments;
        this.presentToday = presentToday;
        this.absentToday = absentToday;
        this.leaveToday = leaveToday;
        this.lateToday = lateToday;
        this.halfDayToday = halfDayToday;
    }

    public long getTotalEmployees() { return totalEmployees; }
    public long getTotalDepartments() { return totalDepartments; }
    public long getPresentToday() { return presentToday; }
    public long getAbsentToday() { return absentToday; }
    public long getLeaveToday() { return leaveToday; }
    public long getLateToday() { return lateToday; }
    public long getHalfDayToday() { return halfDayToday; }

    // ✅ FIXED INNER DTO
    public static class TodayAttendanceDonutDto {

        private long present;
        private long absent;
        private long leave;
        private long halfDay;

        public TodayAttendanceDonutDto(
                long present,
                long absent,
                long leave,
                long halfDay
        ) {
            this.present = present;
            this.absent = absent;
            this.leave = leave;
            this.halfDay = halfDay;
        }

        public long getPresent() { return present; }
        public long getAbsent() { return absent; }
        public long getLeave() { return leave; }
        public long getHalfDay() { return halfDay; }
    }
}