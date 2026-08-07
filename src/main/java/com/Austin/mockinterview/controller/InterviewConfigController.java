package com.Austin.mockinterview.controller;

import com.Austin.mockinterview.dto.interview.CreateInterviewConfigRequest;
import com.Austin.mockinterview.dto.interview.InterviewConfigResponse;
import com.Austin.mockinterview.dto.interview.InterviewOptionsResponse;
import com.Austin.mockinterview.entity.InterviewConfig;
import com.Austin.mockinterview.service.InterviewConfigService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/interview")
public class InterviewConfigController {

    private final InterviewConfigService interviewConfigService;

    public InterviewConfigController(InterviewConfigService interviewConfigService) {
        this.interviewConfigService = interviewConfigService;
    }

    // GET all selectable options
    @GetMapping("/options")
    public ResponseEntity<InterviewOptionsResponse> getOptions() {
        return ResponseEntity.ok(interviewConfigService.getOptions());
    }

    // POST save configuration
//    @PostMapping("/config")
//    public ResponseEntity<InterviewConfig> createConfig(
//            @Valid @RequestBody CreateInterviewConfigRequest request,
//            Authentication authentication) {
//
//        // Assuming your JWT filter sets the username (email) as principal
//        String email = authentication.getName();
//        Long userId = interviewConfigService.getUserIdByEmail(email); // we will add this helper
//
//        InterviewConfig config = interviewConfigService.createConfig(userId, request);
//        return ResponseEntity.ok(config);
//    }
//
//    // GET specific configuration
//    @GetMapping("/config/{id}")
//    public ResponseEntity<InterviewConfig> getConfig(
//            @PathVariable Long id,
//            Authentication authentication) {
//
//        String email = authentication.getName();
//        Long userId = interviewConfigService.getUserIdByEmail(email);
//
//        InterviewConfig config = interviewConfigService.getConfig(id, userId);
//        return ResponseEntity.ok(config);
//    }
    
    @PostMapping("/config")
    public ResponseEntity<InterviewConfigResponse> createConfig(
            @Valid @RequestBody CreateInterviewConfigRequest request,
            Authentication authentication) {

        String email = authentication.getName();
        Long userId = interviewConfigService.getUserIdByEmail(email);

        InterviewConfigResponse response = interviewConfigService.createConfig(userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/config/{id}")
    public ResponseEntity<InterviewConfigResponse> getConfig(
            @PathVariable Long id,
            Authentication authentication) {

        String email = authentication.getName();
        Long userId = interviewConfigService.getUserIdByEmail(email);

        InterviewConfigResponse response = interviewConfigService.getConfig(id, userId);
        return ResponseEntity.ok(response);
    }
}