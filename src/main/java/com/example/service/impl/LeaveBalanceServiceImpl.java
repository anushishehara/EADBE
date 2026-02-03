package com.example.service.impl;

import com.example.model.LeaveBalance;
import com.example.model.LeaveType;
import com.example.model.User;
import com.example.repository.LeaveBalanceRepository;
import com.example.repository.LeaveTypeRepository;
import com.example.repository.UserRepository;
import com.example.service.LeaveBalanceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveBalanceServiceImpl implements LeaveBalanceService {
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final UserRepository userRepository;
    private final LeaveTypeRepository leaveTypeRepository;

    public LeaveBalanceServiceImpl(LeaveBalanceRepository leaveBalanceRepository,
            UserRepository userRepository,
            LeaveTypeRepository leaveTypeRepository) {
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.userRepository = userRepository;
        this.leaveTypeRepository = leaveTypeRepository;
    }

    @Override
    public List<LeaveBalance> getMyLeaveBalances(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<LeaveBalance> balances = leaveBalanceRepository.findByUser(user);

        if (balances.isEmpty()) {
            initializeLeaveBalances(user.getId());
            balances = leaveBalanceRepository.findByUser(user);
        }

        return balances;
    }

    @Override
    public void initializeLeaveBalances(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<LeaveType> leaveTypes = leaveTypeRepository.findAll();

        for (LeaveType leaveType : leaveTypes) {
            if (leaveBalanceRepository.findByUserAndLeaveType(user, leaveType).isEmpty()) {
                LeaveBalance balance = new LeaveBalance(user, leaveType, leaveType.getMaxDays());
                leaveBalanceRepository.save(balance);
            }
        }
    }
}
