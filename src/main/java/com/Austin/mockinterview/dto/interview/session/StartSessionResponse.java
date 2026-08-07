package com.Austin.mockinterview.dto.interview.session;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StartSessionResponse {
    private Long sessionId;
    private String jobRole;
    private String experienceTier;
    private String difficultyLevel;
    private Integer totalQuestions;
    private Integer durationMinutes;
    private CurrentQuestionResponse firstQuestion;
}