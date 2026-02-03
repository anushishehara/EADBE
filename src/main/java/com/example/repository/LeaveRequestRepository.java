package com.example.repository;

import com.example.model.LeaveRequest;
import com.example.model.LeaveStatus;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByUser(User user);

    List<LeaveRequest> findByUserOrderByAppliedDateDesc(User user);

    List<LeaveRequest> findByStatus(LeaveStatus status);

    List<LeaveRequest> findByStatusOrderByAppliedDateDesc(LeaveStatus status);

    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.user = :user AND lr.status = :status " +
            "AND ((lr.startDate <= :endDate AND lr.endDate >= :startDate))")
    List<LeaveRequest> findOverlappingLeaves(@Param("user") User user,
            @Param("status") LeaveStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}
