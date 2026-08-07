package com.Austin.mockinterview.entity;

import com.Austin.mockinterview.enums.SessionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "interview_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class InterviewSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "config_id", nullable = false)
    private InterviewConfig config;

    @Enumerated(EnumType.STRING)
    private SessionStatus status = SessionStatus.IN_PROGRESS;

    private Integer currentQuestionIndex = 0;   // 0-based

    private LocalDateTime startedAt = LocalDateTime.now();
    private LocalDateTime completedAt;

    private Integer totalScore;                 // final overall score out of 100
    private String overallTier;                // e.g. "FAANG L6 PASS TIER"
//    private String executiveSummary;
    @Column(columnDefinition = "TEXT")
    private String executiveSummary;

    // Breakdown scores
    private Integer technicalKnowledgeScore;
    private Integer communicationScore;
    private Integer confidenceScore;
    private Integer problemSolvingScore;

    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("questionOrder ASC")
    private List<InterviewQuestion> questions = new ArrayList<>();
}