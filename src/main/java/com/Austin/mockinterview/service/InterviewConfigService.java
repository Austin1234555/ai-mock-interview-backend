//package com.Austin.mockinterview.service;
//
//import com.Austin.mockinterview.dto.interview.CreateInterviewConfigRequest;
//import com.Austin.mockinterview.dto.interview.InterviewOptionsResponse;
//import com.Austin.mockinterview.entity.InterviewConfig;
//import com.Austin.mockinterview.entity.User;
//import com.Austin.mockinterview.enums.*;
//import com.Austin.mockinterview.repository.InterviewConfigRepository;
//import com.Austin.mockinterview.repository.UserRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class InterviewConfigService {
//
//    private final InterviewConfigRepository configRepository;
//    private final UserRepository userRepository;
//
//    public InterviewConfigService(InterviewConfigRepository configRepository,
//                                  UserRepository userRepository) {
//        this.configRepository = configRepository;
//        this.userRepository = userRepository;
//    }
//
//    // 1. Get all selectable options
//    public InterviewOptionsResponse getOptions() {
//        InterviewOptionsResponse response = new InterviewOptionsResponse();
//
//        response.setJobRoles(
//                Arrays.stream(JobRole.values())
//                        .map(Enum::name)
//                        .collect(Collectors.toList())
//        );
//
//        response.setExperienceTiers(
//                Arrays.stream(ExperienceTier.values())
//                        .map(Enum::name)
//                        .collect(Collectors.toList())
//        );
//
//        response.setDifficultyLevels(
//                Arrays.stream(DifficultyLevel.values())
//                        .map(Enum::name)
//                        .collect(Collectors.toList())
//        );
//
//        response.setDurations(List.of(15, 30, 45, 60));
//        response.setQuestionCounts(List.of(5, 10, 15, 20));
//
//        response.setSuggestedDomains(List.of(
//                "System Architecture & Scalability",
//                "Microservices & Distributed Systems",
//                "Database Design & Optimization",
//                "API Design & RESTful Services",
//                "Concurrency & Multithreading",
//                "Spring Boot Advanced Topics",
//                "Cloud & DevOps Practices"
//        ));
//
//        return response;
//    }
//
//    // 2. Save configuration
////    @Transactional
////    public InterviewConfig createConfig(Long userId, CreateInterviewConfigRequest request) {
////        User user = userRepository.findById(userId)
////                .orElseThrow(() -> new RuntimeException("User not found"));
////
////        InterviewConfig config = new InterviewConfig();
////        config.setUser(user);
////        config.setJobRole(request.getJobRole());
////        config.setExperienceTier(request.getExperienceTier());
////        config.setDifficultyLevel(request.getDifficultyLevel());
////        config.setDurationMinutes(request.getDurationMinutes());
////        config.setNumberOfQuestions(request.getNumberOfQuestions());
////        config.setCoreDomainFocus(request.getCoreDomainFocus());
////        config.setIncludeCodingChallenge(request.isIncludeCodingChallenge());
////
////        return configRepository.save(config);
////    }
////
////    // 3. Get config by id (only if belongs to user)
////    public InterviewConfig getConfig(Long configId, Long userId) {
////        return configRepository.findByIdAndUserId(configId, userId)
////                .orElseThrow(() -> new RuntimeException("Configuration not found"));
////    }
////
////    public Long getUserIdByEmail(String email) {
////        return userRepository.findByEmail(email)
////                .orElseThrow(() -> new RuntimeException("User not found"))
////                .getId();
////    }
////}
//
//
//    @Transactional
//    public InterviewConfigResponse createConfig(Long userId, CreateInterviewConfigRequest request) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        InterviewConfig config = new InterviewConfig();
//        config.setUser(user);
//        config.setJobRole(request.getJobRole());
//        config.setExperienceTier(request.getExperienceTier());
//        config.setDifficultyLevel(request.getDifficultyLevel());
//        config.setDurationMinutes(request.getDurationMinutes());
//        config.setNumberOfQuestions(request.getNumberOfQuestions());
//        config.setCoreDomainFocus(request.getCoreDomainFocus());
//        config.setIncludeCodingChallenge(request.isIncludeCodingChallenge());
//
//        InterviewConfig saved = configRepository.save(config);
//        return mapToResponse(saved);
//    }
//
//    // Also update getConfig method
//    public InterviewConfigResponse getConfig(Long configId, Long userId) {
//        InterviewConfig config = configRepository.findByIdAndUserId(configId, userId)
//                .orElseThrow(() -> new RuntimeException("Configuration not found"));
//        return mapToResponse(config);
//    }
//
//    // Helper method
//    private InterviewConfigResponse mapToResponse(InterviewConfig config) {
//        InterviewConfigResponse response = new InterviewConfigResponse();
//        response.setId(config.getId());
//        response.setJobRole(config.getJobRole());
//        response.setExperienceTier(config.getExperienceTier());
//        response.setDifficultyLevel(config.getDifficultyLevel());
//        response.setDurationMinutes(config.getDurationMinutes());
//        response.setNumberOfQuestions(config.getNumberOfQuestions());
//        response.setCoreDomainFocus(config.getCoreDomainFocus());
//        response.setIncludeCodingChallenge(config.isIncludeCodingChallenge());
//        response.setCreatedAt(config.getCreatedAt());
//        return response;
//
//
//        public Long getUserIdByEmail(String email) {
//            return userRepository.findByEmail(email)
//                    .orElseThrow(() -> new RuntimeException("User not found"))
//                    .getId();
//        }
//    }
//



package com.Austin.mockinterview.service;

import com.Austin.mockinterview.dto.interview.CreateInterviewConfigRequest;
import com.Austin.mockinterview.dto.interview.InterviewConfigResponse;
import com.Austin.mockinterview.dto.interview.InterviewOptionsResponse;
import com.Austin.mockinterview.entity.InterviewConfig;
import com.Austin.mockinterview.entity.User;
import com.Austin.mockinterview.enums.*;
import com.Austin.mockinterview.repository.InterviewConfigRepository;
import com.Austin.mockinterview.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterviewConfigService {

    private final InterviewConfigRepository configRepository;
    private final UserRepository userRepository;

    public InterviewConfigService(InterviewConfigRepository configRepository,
                                  UserRepository userRepository) {
        this.configRepository = configRepository;
        this.userRepository = userRepository;
    }

    // 1. Get all selectable options
    public InterviewOptionsResponse getOptions() {
        InterviewOptionsResponse response = new InterviewOptionsResponse();

        response.setJobRoles(
                Arrays.stream(JobRole.values())
                        .map(Enum::name)
                        .collect(Collectors.toList())
        );

        response.setExperienceTiers(
                Arrays.stream(ExperienceTier.values())
                        .map(Enum::name)
                        .collect(Collectors.toList())
        );

        response.setDifficultyLevels(
                Arrays.stream(DifficultyLevel.values())
                        .map(Enum::name)
                        .collect(Collectors.toList())
        );

        response.setDurations(List.of(15, 30, 45, 60));
        response.setQuestionCounts(List.of(5, 10, 15, 20));

        response.setSuggestedDomains(List.of(
                "System Architecture & Scalability",
                "Microservices & Distributed Systems",
                "Database Design & Optimization",
                "API Design & RESTful Services",
                "Concurrency & Multithreading",
                "Spring Boot Advanced Topics",
                "Cloud & DevOps Practices"
        ));

        return response;
    }

    // 2. Save configuration
    @Transactional
    public InterviewConfigResponse createConfig(Long userId, CreateInterviewConfigRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        InterviewConfig config = new InterviewConfig();
        config.setUser(user);
        config.setJobRole(request.getJobRole());
        config.setExperienceTier(request.getExperienceTier());
        config.setDifficultyLevel(request.getDifficultyLevel());
        config.setDurationMinutes(request.getDurationMinutes());
        config.setNumberOfQuestions(request.getNumberOfQuestions());
        config.setCoreDomainFocus(request.getCoreDomainFocus());
        config.setIncludeCodingChallenge(request.isIncludeCodingChallenge());

        InterviewConfig saved = configRepository.save(config);
        return mapToResponse(saved);
    }

    // 3. Get config by id
    public InterviewConfigResponse getConfig(Long configId, Long userId) {
        InterviewConfig config = configRepository.findByIdAndUserId(configId, userId)
                .orElseThrow(() -> new RuntimeException("Configuration not found"));
        return mapToResponse(config);
    }

    // Helper method
    private InterviewConfigResponse mapToResponse(InterviewConfig config) {
        InterviewConfigResponse response = new InterviewConfigResponse();
        response.setId(config.getId());
        response.setJobRole(config.getJobRole());
        response.setExperienceTier(config.getExperienceTier());
        response.setDifficultyLevel(config.getDifficultyLevel());
        response.setDurationMinutes(config.getDurationMinutes());
        response.setNumberOfQuestions(config.getNumberOfQuestions());
        response.setCoreDomainFocus(config.getCoreDomainFocus());
        response.setIncludeCodingChallenge(config.isIncludeCodingChallenge());
        response.setCreatedAt(config.getCreatedAt());
        return response;
    }

    public Long getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }
}