package com.Austin.mockinterview.controller;

import com.Austin.mockinterview.dto.interview.session.*;
import com.Austin.mockinterview.service.InterviewSessionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interview/session")
public class InterviewSessionController {

    @Autowired
    private InterviewSessionService sessionService;

    // Start a new session from a config
    @PostMapping("/start/{configId}")
    public ResponseEntity<StartSessionResponse> startSession(@PathVariable Long configId) {
        return ResponseEntity.ok(sessionService.startSession(configId));
    }

    // Submit answer for a question
    @PostMapping("/{sessionId}/question/{questionId}/answer")
    public ResponseEntity<QuestionEvaluationResponse> submitAnswer(
            @PathVariable Long sessionId,
            @PathVariable Long questionId,
            @Valid @RequestBody SubmitAnswerRequest request) {
        return ResponseEntity.ok(sessionService.submitAnswer(sessionId, questionId, request));
    }

    // Finish session and get final report
    @PostMapping("/{sessionId}/finish")
    public ResponseEntity<FinalReportResponse> finishSession(@PathVariable Long sessionId) {
        return ResponseEntity.ok(sessionService.finishSession(sessionId));
    }
}