package com.example.service.impl;

import com.example.dto.LeaveApplicationRequest;
import com.example.dto.LeaveApprovalRequest;
import com.example.model.*;
import com.example.repository.LeaveBalanceRepository;
import com.example.repository.LeaveRequestRepository;
import com.example.repository.LeaveTypeRepository;
import com.example.repository.UserRepository;
import com.example.service.LeaveRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {
    private final LeaveRequestRepository leaveRequestRepository;
    private final UserRepository userRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final com.example.service.LeaveBalanceService leaveBalanceService;

    public LeaveRequestServiceImpl(LeaveRequestRepository leaveRequestRepository,
            UserRepository userRepository,
            LeaveTypeRepository leaveTypeRepository,
            LeaveBalanceRepository leaveBalanceRepository,
            com.example.service.LeaveBalanceService leaveBalanceService) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.userRepository = userRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.leaveBalanceService = leaveBalanceService;
    }

    @Override
    @Transactional
    public LeaveRequest applyLeave(LeaveApplicationRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LeaveType leaveType = leaveTypeRepository.findById(request.getLeaveTypeId())
                .orElseThrow(() -> new RuntimeException("Leave type not found"));

        // Calculate total days
        int totalDays = (int) ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate()) + 1;

        // Check for overlapping leaves
        List<LeaveRequest> overlapping = leaveRequestRepository.findOverlappingLeaves(
                user, LeaveStatus.APPROVED, request.getStartDate(), request.getEndDate());

        if (!overlapping.isEmpty()) {
            throw new RuntimeException("Leave request overlaps with existing approved leave");
        }

        // Check leave balance (Initialize if missing)
        LeaveBalance balance = leaveBalanceRepository.findByUserAndLeaveType(user, leaveType)
                .orElseGet(() -> {
                    leaveBalanceService.initializeLeaveBalances(user.getId());
                    return leaveBalanceRepository.findByUserAndLeaveType(user, leaveType)
                            .orElseThrow(() -> new RuntimeException("Could not initialize leave balance"));
                });

        if (balance.getRemainingDays() < totalDays) {
            throw new RuntimeException(
                    "Insufficient leave balance. Available: " + balance.getRemainingDays() + " days");
        }

        // Create leave request
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setUser(user);
        leaveRequest.setLeaveType(leaveType);
        leaveRequest.setStartDate(request.getStartDate());
        leaveRequest.setEndDate(request.getEndDate());
        leaveRequest.setReason(request.getReason());
        leaveRequest.setTotalDays(totalDays);
        leaveRequest.setStatus(LeaveStatus.PENDING);

        return leaveRequestRepository.save(leaveRequest);
    }

    @Override
    public List<LeaveRequest> getMyLeaves(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return leaveRequestRepository.findByUserOrderByAppliedDateDesc(user);
    }

    @Override
    public List<LeaveRequest> getAllPendingLeaves() {
        return leaveRequestRepository.findByStatusOrderByAppliedDateDesc(LeaveStatus.PENDING);
    }

    @Override
    public List<LeaveRequest> getAllLeaves() {
        return leaveRequestRepository.findAll();
    }

    @Override
    public LeaveRequest getLeaveById(Long id) {
        return leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));
    }

    @Override
    @Transactional
    public LeaveRequest approveOrRejectLeave(Long id, LeaveApprovalRequest request, String username) {
        LeaveRequest leaveRequest = getLeaveById(id);
        User approver = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (leaveRequest.getStatus() != LeaveStatus.PENDING) {
            throw new RuntimeException("Leave request has already been processed");
        }

        LeaveStatus newStatus = LeaveStatus.valueOf(request.getStatus().toUpperCase());
        leaveRequest.setStatus(newStatus);
        leaveRequest.setRemarks(request.getRemarks());
        leaveRequest.setProcessedBy(approver);
        leaveRequest.setProcessedDate(LocalDateTime.now());

        // Update leave balance if approved
        if (newStatus == LeaveStatus.APPROVED) {
            LeaveBalance balance = leaveBalanceRepository
                    .findByUserAndLeaveType(leaveRequest.getUser(), leaveRequest.getLeaveType())
                    .orElseThrow(() -> new RuntimeException("Leave balance not found"));

            balance.setUsedDays(balance.getUsedDays() + leaveRequest.getTotalDays());
            balance.setRemainingDays(balance.getRemainingDays() - leaveRequest.getTotalDays());
            leaveBalanceRepository.save(balance);
        }

        return leaveRequestRepository.save(leaveRequest);
    }

    @Override
    @Transactional
    public void cancelLeave(Long id, String username) {
        LeaveRequest leaveRequest = getLeaveById(id);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!leaveRequest.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You can only cancel your own leave requests");
        }

        if (leaveRequest.getStatus() == LeaveStatus.APPROVED) {
            // Restore leave balance
            LeaveBalance balance = leaveBalanceRepository
                    .findByUserAndLeaveType(user, leaveRequest.getLeaveType())
                    .orElseThrow(() -> new RuntimeException("Leave balance not found"));

            balance.setUsedDays(balance.getUsedDays() - leaveRequest.getTotalDays());
            balance.setRemainingDays(balance.getRemainingDays() + leaveRequest.getTotalDays());
            leaveBalanceRepository.save(balance);
        }

        leaveRequest.setStatus(LeaveStatus.CANCELLED);
        leaveRequestRepository.save(leaveRequest);
    }
}
