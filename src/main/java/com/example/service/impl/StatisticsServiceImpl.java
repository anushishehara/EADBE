package com.example.service.impl;

import com.example.dto.AdminDashboardStatsDTO;
import com.example.model.LeaveRequest;
import com.example.model.LeaveStatus;
import com.example.repository.LeaveRequestRepository;
import com.example.repository.UserRepository;
import com.example.service.StatisticsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final UserRepository userRepository;
    private final LeaveRequestRepository leaveRequestRepository;

    public StatisticsServiceImpl(UserRepository userRepository, LeaveRequestRepository leaveRequestRepository) {
        this.userRepository = userRepository;
        this.leaveRequestRepository = leaveRequestRepository;
    }

    @Override
    public AdminDashboardStatsDTO getAdminDashboardStats() {
        long totalEmployees = userRepository.count();
        List<LeaveRequest> allLeaves = leaveRequestRepository.findAll();

        long pendingLeaves = allLeaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.PENDING)
                .count();

        long rejectedLeaves = allLeaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.REJECTED)
                .count();

        LocalDate today = LocalDate.now();
        long approvedToday = allLeaves.stream()
                .filter(l -> l.getStatus() == LeaveStatus.APPROVED &&
                        l.getProcessedDate() != null &&
                        l.getProcessedDate().toLocalDate().isEqual(today))
                .count();

        Map<String, Long> leavesByType = allLeaves.stream()
                .collect(Collectors.groupingBy(
                        l -> l.getLeaveType().getTypeName(),
                        Collectors.counting()));

        Map<String, Long> leavesByStatus = allLeaves.stream()
                .collect(Collectors.groupingBy(
                        l -> l.getStatus().name(),
                        Collectors.counting()));

        return new AdminDashboardStatsDTO(
                totalEmployees,
                pendingLeaves,
                approvedToday,
                rejectedLeaves,
                leavesByType,
                leavesByStatus);
    }
}
