package com.Austin.mockinterview.dto.interview.session;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestionEvaluationResponse {

    private Long questionId;
    private Integer questionNumber;
    private Integer totalQuestions;

    // Score card
    private Integer score;                       // 65
    private String attemptLabel;                 // "Solid Attempt"
    private Integer matchedRubricMetrics;        // 2
    private Integer totalRubricMetrics;          // 4

    private String aiCritique;

    private List<String> demonstratedStrengths;
    private String growthOpportunity;

    private String modelAnswer;                  // optional - can be collapsed in UI

    private boolean isLastQuestion;
    private CurrentQuestionResponse nextQuestion; // null if last
}