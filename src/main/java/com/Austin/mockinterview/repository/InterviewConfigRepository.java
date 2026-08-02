package com.Austin.mockinterview.repository;

import com.Austin.mockinterview.entity.InterviewConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewConfigRepository extends JpaRepository<InterviewConfig, Long> {

    List<InterviewConfig> findByUserId(Long userId);

    Optional<InterviewConfig> findByIdAndUserId(Long id, Long userId);
}