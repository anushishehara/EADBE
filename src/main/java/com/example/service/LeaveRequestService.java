package com.example.service;

import com.example.dto.LeaveApplicationRequest;
import com.example.dto.LeaveApprovalRequest;
import com.example.model.LeaveRequest;

import java.util.List;

public interface LeaveRequestService {
    LeaveRequest applyLeave(LeaveApplicationRequest request, String username);

    List<LeaveRequest> getMyLeaves(String username);

    List<LeaveRequest> getAllPendingLeaves();

    List<LeaveRequest> getAllLeaves();

    LeaveRequest getLeaveById(Long id);

    LeaveRequest approveOrRejectLeave(Long id, LeaveApprovalRequest request, String username);

    void cancelLeave(Long id, String username);
}
