package com.Austin.mockinterview.dto.interview.session;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FinalReportResponse {

    private Long sessionId;
    private String jobRole;
    private String experienceTier;
    private String completedAt;
    private String timeTaken;                    // "28m 42s"

    // Overall
    private Integer overallScore;                // 92
    private String overallTier;                 // "FAANG L6 PASS TIER"
    private String executiveSummary;

    // Breakdown
    private Integer technicalKnowledgeScore;
    private Integer communicationScore;
    private Integer confidenceScore;
    private Integer problemSolvingScore;

    // Lists
    private List<String> demonstratedStrengths;
    private List<String> areasForImprovement;
    private List<String> recommendedTopics;

    // Question-wise
    private List<QuestionSummary> questionSummaries;

    @Data
    @Builder
    public static class QuestionSummary {
        private Integer questionNumber;
        private String questionText;
        private Integer score;
        private String aiCritique;
    }
}