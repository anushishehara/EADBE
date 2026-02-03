package com.example.dto;

import java.util.Map;

public class AdminDashboardStatsDTO {
    private long totalEmployees;
    private long pendingLeaves;
    private long approvedLeavesToday;
    private long rejectedLeaves;
    private Map<String, Long> leavesByType;
    private Map<String, Long> leavesByStatus;

    public AdminDashboardStatsDTO() {
    }

    public AdminDashboardStatsDTO(long totalEmployees, long pendingLeaves, long approvedLeavesToday,
            long rejectedLeaves, Map<String, Long> leavesByType,
            Map<String, Long> leavesByStatus) {
        this.totalEmployees = totalEmployees;
        this.pendingLeaves = pendingLeaves;
        this.approvedLeavesToday = approvedLeavesToday;
        this.rejectedLeaves = rejectedLeaves;
        this.leavesByType = leavesByType;
        this.leavesByStatus = leavesByStatus;
    }

    public long getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public long getPendingLeaves() {
        return pendingLeaves;
    }

    public void setPendingLeaves(long pendingLeaves) {
        this.pendingLeaves = pendingLeaves;
    }

    public long getApprovedLeavesToday() {
        return approvedLeavesToday;
    }

    public void setApprovedLeavesToday(long approvedLeavesToday) {
        this.approvedLeavesToday = approvedLeavesToday;
    }

    public long getRejectedLeaves() {
        return rejectedLeaves;
    }

    public void setRejectedLeaves(long rejectedLeaves) {
        this.rejectedLeaves = rejectedLeaves;
    }

    public Map<String, Long> getLeavesByType() {
        return leavesByType;
    }

    public void setLeavesByType(Map<String, Long> leavesByType) {
        this.leavesByType = leavesByType;
    }

    public Map<String, Long> getLeavesByStatus() {
        return leavesByStatus;
    }

    public void setLeavesByStatus(Map<String, Long> leavesByStatus) {
        this.leavesByStatus = leavesByStatus;
    }
}
