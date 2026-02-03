package com.example.service;

import com.example.model.LeaveBalance;

import java.util.List;

public interface LeaveBalanceService {
    List<LeaveBalance> getMyLeaveBalances(String username);

    void initializeLeaveBalances(Long userId);
}
