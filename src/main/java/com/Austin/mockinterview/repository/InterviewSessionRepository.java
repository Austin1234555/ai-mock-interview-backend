package com.Austin.mockinterview.repository;

import com.Austin.mockinterview.entity.InterviewSession;
import com.Austin.mockinterview.enums.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InterviewSessionRepository extends JpaRepository<InterviewSession, Long> {

    List<InterviewSession> findByUserIdOrderByStartedAtDesc(Long userId);

    Optional<InterviewSession> findByIdAndUserId(Long id, Long userId);

    Optional<InterviewSession> findByIdAndUserIdAndStatus(Long id, Long userId, SessionStatus status);
}