package com.example.controller;

import com.example.dto.LeaveApplicationRequest;
import com.example.dto.LeaveApprovalRequest;
import com.example.model.LeaveRequest;
import com.example.service.LeaveRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveRequestController {
    private final LeaveRequestService leaveRequestService;

    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }

    @PostMapping("/apply")
    public ResponseEntity<?> applyLeave(@RequestBody LeaveApplicationRequest request, Authentication authentication) {
        try {
            LeaveRequest leaveRequest = leaveRequestService.applyLeave(request, authentication.getName());
            return ResponseEntity.ok(leaveRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/my-leaves")
    public ResponseEntity<List<LeaveRequest>> getMyLeaves(Authentication authentication) {
        return ResponseEntity.ok(leaveRequestService.getMyLeaves(authentication.getName()));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LeaveRequest>> getAllPendingLeaves() {
        return ResponseEntity.ok(leaveRequestService.getAllPendingLeaves());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LeaveRequest>> getAllLeaves() {
        return ResponseEntity.ok(leaveRequestService.getAllLeaves());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeaveRequest> getLeaveById(@PathVariable Long id) {
        return ResponseEntity.ok(leaveRequestService.getLeaveById(id));
    }

    @PutMapping("/{id}/process")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> processLeave(@PathVariable Long id,
            @RequestBody LeaveApprovalRequest request,
            Authentication authentication) {
        try {
            LeaveRequest leaveRequest = leaveRequestService.approveOrRejectLeave(id, request, authentication.getName());
            return ResponseEntity.ok(leaveRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<?> cancelLeave(@PathVariable Long id, Authentication authentication) {
        try {
            leaveRequestService.cancelLeave(id, authentication.getName());
            return ResponseEntity.ok("Leave request cancelled successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
