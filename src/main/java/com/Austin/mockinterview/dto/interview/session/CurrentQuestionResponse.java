package com.Austin.mockinterview.dto.interview.session;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CurrentQuestionResponse {
    private Long questionId;
    private Integer questionNumber;          // 1-based
    private Integer totalQuestions;
    private String topic;
    private String questionText;
    private String aiProbeHint;
    private String difficultyLabel;          // "Hard Tier"
    private String rubricName;               // "FAANG System Architecture Rubric"
    private Integer remainingSeconds;        // optional timer
}