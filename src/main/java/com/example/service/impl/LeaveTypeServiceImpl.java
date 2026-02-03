package com.example.service.impl;

import com.example.dto.LeaveTypeRequest;
import com.example.model.LeaveType;
import com.example.repository.LeaveTypeRepository;
import com.example.service.LeaveTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveTypeServiceImpl implements LeaveTypeService {
    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveTypeServiceImpl(LeaveTypeRepository leaveTypeRepository) {
        this.leaveTypeRepository = leaveTypeRepository;
    }

    @Override
    public LeaveType createLeaveType(LeaveTypeRequest request) {
        if (leaveTypeRepository.existsByTypeName(request.getTypeName())) {
            throw new RuntimeException("Leave type already exists");
        }

        LeaveType leaveType = new LeaveType(
                request.getTypeName(),
                request.getMaxDays(),
                request.getDescription());

        return leaveTypeRepository.save(leaveType);
    }

    @Override
    public List<LeaveType> getAllLeaveTypes() {
        return leaveTypeRepository.findAll();
    }

    @Override
    public LeaveType getLeaveTypeById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Leave type ID cannot be null");
        }
        return leaveTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave type not found"));
    }

    @Override
    public LeaveType updateLeaveType(Long id, LeaveTypeRequest request) {
        LeaveType leaveType = getLeaveTypeById(id);
        leaveType.setTypeName(request.getTypeName());
        leaveType.setMaxDays(request.getMaxDays());
        leaveType.setDescription(request.getDescription());
        return leaveTypeRepository.save(leaveType);
    }

    @Override
    public void deleteLeaveType(Long id) {
        leaveTypeRepository.deleteById(id);
    }
}
