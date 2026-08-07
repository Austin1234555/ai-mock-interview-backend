package com.Austin.mockinterview.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "interview_questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private InterviewSession session;

    private Integer questionOrder;               // 1, 2, 3...

    private String topic;                        // e.g. "JVM Memory Management"
    private String questionText;
    private String aiProbeHint;
    private String difficultyLabel;              // "Hard Tier"
    private String rubricName;                   // "FAANG System Architecture Rubric"

    // User answer
    @Column(columnDefinition = "TEXT")
    private String userAnswer;

    private LocalDateTime answeredAt;

    // Immediate AI Evaluation
    private Integer score;                       // 0-100
    private String attemptLabel;                 // "Solid Attempt"
    private Integer matchedRubricMetrics;        // e.g. 2
    private Integer totalRubricMetrics;          // e.g. 4

    @Column(columnDefinition = "TEXT")
    private String aiCritique;

    @Column(columnDefinition = "TEXT")
    private String demonstratedStrengths;        // JSON or newline separated

    @Column(columnDefinition = "TEXT")
    private String growthOpportunity;

    @Column(columnDefinition = "TEXT")
    private String modelAnswer;                  // L6 FAANG model answer
}