package com.example.repository;

import com.example.model.LeaveBalance;
import com.example.model.LeaveType;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveBalanceRepository extends JpaRepository<LeaveBalance, Long> {
    List<LeaveBalance> findByUser(User user);

    Optional<LeaveBalance> findByUserAndLeaveType(User user, LeaveType leaveType);
}
