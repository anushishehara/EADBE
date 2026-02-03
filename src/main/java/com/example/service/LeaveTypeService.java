package com.example.service;

import com.example.dto.LeaveTypeRequest;
import com.example.model.LeaveType;

import java.util.List;

public interface LeaveTypeService {
    LeaveType createLeaveType(LeaveTypeRequest request);

    List<LeaveType> getAllLeaveTypes();

    LeaveType getLeaveTypeById(Long id);

    LeaveType updateLeaveType(Long id, LeaveTypeRequest request);

    void deleteLeaveType(Long id);
}
