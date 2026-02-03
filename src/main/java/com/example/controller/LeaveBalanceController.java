package com.example.controller;

import com.example.model.LeaveBalance;
import com.example.service.LeaveBalanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leave-balances")
public class LeaveBalanceController {
    private final LeaveBalanceService leaveBalanceService;

    public LeaveBalanceController(LeaveBalanceService leaveBalanceService) {
        this.leaveBalanceService = leaveBalanceService;
    }

    @GetMapping("/my-balances")
    public ResponseEntity<List<LeaveBalance>> getMyLeaveBalances(Authentication authentication) {
        return ResponseEntity.ok(leaveBalanceService.getMyLeaveBalances(authentication.getName()));
    }
}
