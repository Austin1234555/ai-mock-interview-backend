package com.Austin.mockinterview.service;

import com.Austin.mockinterview.dto.interview.session.*;
import com.Austin.mockinterview.entity.*;
import com.Austin.mockinterview.enums.SessionStatus;
import com.Austin.mockinterview.repository.InterviewConfigRepository;
import com.Austin.mockinterview.repository.InterviewQuestionRepository;
import com.Austin.mockinterview.repository.InterviewSessionRepository;
import com.Austin.mockinterview.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InterviewSessionService {

    @Autowired
    private InterviewSessionRepository sessionRepository;

    @Autowired
    private InterviewQuestionRepository questionRepository;

    @Autowired
    private InterviewConfigRepository configRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LlmService llmService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // ================= START SESSION =================
    @Transactional
    public StartSessionResponse startSession(Long configId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        InterviewConfig config = configRepository.findById(configId)
                .orElseThrow(() -> new RuntimeException("Config not found"));

        if (!config.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't own this configuration");
        }

        // Create session
        InterviewSession session = InterviewSession.builder()
                .user(user)
                .config(config)
                .status(SessionStatus.IN_PROGRESS)
                .currentQuestionIndex(0)
                .startedAt(LocalDateTime.now())
                .build();

        session = sessionRepository.save(session);

        // Generate questions using AI
        List<InterviewQuestion> questions = generateQuestions(session, config);
        session.setQuestions(questions);
        sessionRepository.save(session);

        InterviewQuestion first = questions.get(0);

        return StartSessionResponse.builder()
                .sessionId(session.getId())
                .jobRole(config.getJobRole().name())
                .experienceTier(config.getExperienceTier().name())
                .difficultyLevel(config.getDifficultyLevel().name())
                .totalQuestions(config.getNumberOfQuestions())
                .durationMinutes(config.getDurationMinutes())
                .firstQuestion(mapToCurrentQuestion(first, config.getNumberOfQuestions()))
                .build();
    }

    // ================= SUBMIT ANSWER + EVALUATE =================
    @Transactional
    public QuestionEvaluationResponse submitAnswer(Long sessionId, Long questionId, SubmitAnswerRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        InterviewSession session = sessionRepository.findByIdAndUserIdAndStatus(
                        sessionId, user.getId(), SessionStatus.IN_PROGRESS)
                .orElseThrow(() -> new RuntimeException("Active session not found"));

        InterviewQuestion question = questionRepository.findByIdAndSessionId(questionId, sessionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));

        if (question.getUserAnswer() != null) {
            throw new RuntimeException("This question is already answered");
        }

        // Save user answer
        question.setUserAnswer(request.getAnswer());
        question.setAnsweredAt(LocalDateTime.now());

        // Call AI for evaluation
        evaluateAnswer(question, session.getConfig());

        questionRepository.save(question);

        // Move to next question index
        int nextIndex = session.getCurrentQuestionIndex() + 1;
        session.setCurrentQuestionIndex(nextIndex);
        sessionRepository.save(session);

        boolean isLast = nextIndex >= session.getQuestions().size();

        CurrentQuestionResponse nextQuestionDto = null;
        if (!isLast) {
            InterviewQuestion nextQ = session.getQuestions().get(nextIndex);
            nextQuestionDto = mapToCurrentQuestion(nextQ, session.getQuestions().size());
        }

        return QuestionEvaluationResponse.builder()
                .questionId(question.getId())
                .questionNumber(question.getQuestionOrder())
                .totalQuestions(session.getQuestions().size())
                .score(question.getScore())
                .attemptLabel(question.getAttemptLabel())
                .matchedRubricMetrics(question.getMatchedRubricMetrics())
                .totalRubricMetrics(question.getTotalRubricMetrics())
                .aiCritique(question.getAiCritique())
                .demonstratedStrengths(splitToList(question.getDemonstratedStrengths()))
                .growthOpportunity(question.getGrowthOpportunity())
                .modelAnswer(question.getModelAnswer())
                .isLastQuestion(isLast)
                .nextQuestion(nextQuestionDto)
                .build();
    }

    // ================= FINISH SESSION + FINAL REPORT =================
    @Transactional
    public FinalReportResponse finishSession(Long sessionId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        InterviewSession session = sessionRepository.findByIdAndUserId(sessionId, user.getId())
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.getStatus() == SessionStatus.COMPLETED) {
            return buildFinalReport(session);
        }

        // Make sure all questions are answered
        boolean allAnswered = session.getQuestions().stream()
                .allMatch(q -> q.getUserAnswer() != null);

        if (!allAnswered) {
            throw new RuntimeException("Please answer all questions before finishing");
        }

        // Generate final report using AI
        generateFinalReport(session);

        session.setStatus(SessionStatus.COMPLETED);
        session.setCompletedAt(LocalDateTime.now());
        sessionRepository.save(session);

        return buildFinalReport(session);
    }

    // ================= HELPERS =================

    private List<InterviewQuestion> generateQuestions(InterviewSession session, InterviewConfig config) {
        String systemPrompt = """
                You are an expert FAANG-level technical interviewer.
                Generate high-quality interview questions.
                Return ONLY a valid JSON array. No extra text.
                Each object must have:
                {
                  "topic": "short topic name",
                  "questionText": "full question",
                  "aiProbeHint": "one useful hint",
                  "difficultyLabel": "Hard Tier / Medium Tier / Easy Tier",
                  "rubricName": "FAANG System Architecture Rubric"
                }
                """;

        String userPrompt = String.format("""
                Generate exactly %d interview questions for:
                Job Role: %s
                Experience: %s
                Difficulty: %s
                Core Domain Focus: %s
                Include Coding Challenge: %s
                """,
                config.getNumberOfQuestions(),
                config.getJobRole(),
                config.getExperienceTier(),
                config.getDifficultyLevel(),
                config.getCoreDomainFocus() != null ? config.getCoreDomainFocus() : "General",
                config.isIncludeCodingChallenge()
        );

        String raw = llmService.chat(systemPrompt, userPrompt);

        try {
            // Clean possible markdown
            raw = raw.replaceAll("```json", "").replaceAll("```", "").trim();
            List<Map<String, String>> list = objectMapper.readValue(raw, new TypeReference<>() {});

            List<InterviewQuestion> questions = new ArrayList<>();
            int order = 1;
            for (Map<String, String> item : list) {
                InterviewQuestion q = InterviewQuestion.builder()
                        .session(session)
                        .questionOrder(order++)
                        .topic(item.get("topic"))
                        .questionText(item.get("questionText"))
                        .aiProbeHint(item.get("aiProbeHint"))
                        .difficultyLabel(item.get("difficultyLabel"))
                        .rubricName(item.get("rubricName"))
                        .build();
                questions.add(q);
            }
            return questions;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse AI questions: " + e.getMessage());
        }
    }

    private void evaluateAnswer(InterviewQuestion question, InterviewConfig config) {
        String systemPrompt = """
                You are a strict FAANG L5/L6 interviewer.
                Evaluate the candidate's answer.
                Return ONLY valid JSON with these fields:
                {
                  "score": 0-100,
                  "attemptLabel": "Excellent / Strong / Solid Attempt / Needs Improvement / Poor",
                  "matchedRubricMetrics": number,
                  "totalRubricMetrics": 4,
                  "aiCritique": "2-3 sentence critique",
                  "demonstratedStrengths": ["point1", "point2"],
                  "growthOpportunity": "one clear improvement area",
                  "modelAnswer": "concise high-quality model answer"
                }
                """;

        String userPrompt = String.format("""
                Job Role: %s
                Experience: %s
                Difficulty: %s
                Topic: %s
                Question: %s
                Candidate Answer: %s
                """,
                config.getJobRole(),
                config.getExperienceTier(),
                config.getDifficultyLevel(),
                question.getTopic(),
                question.getQuestionText(),
                question.getUserAnswer()
        );

        String raw = llmService.chat(systemPrompt, userPrompt);
        try {
            raw = raw.replaceAll("```json", "").replaceAll("```", "").trim();
            Map<String, Object> eval = objectMapper.readValue(raw, new TypeReference<>() {});

            question.setScore(((Number) eval.get("score")).intValue());
            question.setAttemptLabel((String) eval.get("attemptLabel"));
            question.setMatchedRubricMetrics(((Number) eval.get("matchedRubricMetrics")).intValue());
            question.setTotalRubricMetrics(((Number) eval.get("totalRubricMetrics")).intValue());
            question.setAiCritique((String) eval.get("aiCritique"));
            question.setGrowthOpportunity((String) eval.get("growthOpportunity"));
            question.setModelAnswer((String) eval.get("modelAnswer"));

            List<String> strengths = (List<String>) eval.get("demonstratedStrengths");
            question.setDemonstratedStrengths(String.join("\n", strengths));
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse evaluation: " + e.getMessage());
        }
    }

    private void generateFinalReport(InterviewSession session) {
        // Simple average for now + AI summary
        List<InterviewQuestion> questions = session.getQuestions();
        double avg = questions.stream().mapToInt(InterviewQuestion::getScore).average().orElse(0);

        session.setTotalScore((int) Math.round(avg));
        session.setOverallTier(avg >= 85 ? "FAANG L6 PASS TIER" :
                avg >= 70 ? "FAANG L5 PASS TIER" : "NEEDS IMPROVEMENT");

        session.setTechnicalKnowledgeScore((int) avg);
        session.setCommunicationScore((int) (avg * 0.95));
        session.setConfidenceScore((int) (avg * 0.92));
        session.setProblemSolvingScore((int) (avg * 0.90));

        // Generate executive summary with AI
        String system = "You are an expert interviewer. Write a short executive summary (2-3 sentences) of the candidate's performance.";
        String user = "Overall score: " + session.getTotalScore() + "/100. Role: " + session.getConfig().getJobRole();
        String summary = llmService.chat(system, user);
        session.setExecutiveSummary(summary);
    }

    private FinalReportResponse buildFinalReport(InterviewSession session) {
        Duration duration = Duration.between(session.getStartedAt(), session.getCompletedAt());
        String timeTaken = duration.toMinutes() + "m " + (duration.toSecondsPart()) + "s";

        List<FinalReportResponse.QuestionSummary> summaries = session.getQuestions().stream()
                .map(q -> FinalReportResponse.QuestionSummary.builder()
                        .questionNumber(q.getQuestionOrder())
                        .questionText(q.getQuestionText())
                        .score(q.getScore())
                        .aiCritique(q.getAiCritique())
                        .build())
                .collect(Collectors.toList());

        // Collect strengths & improvements
        List<String> strengths = new ArrayList<>();
        List<String> improvements = new ArrayList<>();
        for (InterviewQuestion q : session.getQuestions()) {
            if (q.getDemonstratedStrengths() != null) {
                strengths.addAll(splitToList(q.getDemonstratedStrengths()));
            }
            if (q.getGrowthOpportunity() != null) {
                improvements.add(q.getGrowthOpportunity());
            }
        }

        return FinalReportResponse.builder()
                .sessionId(session.getId())
                .jobRole(session.getConfig().getJobRole().name())
                .experienceTier(session.getConfig().getExperienceTier().name())
                .completedAt(session.getCompletedAt().format(DateTimeFormatter.ofPattern("MMM d, yyyy")))
                .timeTaken(timeTaken)
                .overallScore(session.getTotalScore())
                .overallTier(session.getOverallTier())
                .executiveSummary(session.getExecutiveSummary())
                .technicalKnowledgeScore(session.getTechnicalKnowledgeScore())
                .communicationScore(session.getCommunicationScore())
                .confidenceScore(session.getConfidenceScore())
                .problemSolvingScore(session.getProblemSolvingScore())
                .demonstratedStrengths(strengths.stream().distinct().limit(5).collect(Collectors.toList()))
                .areasForImprovement(improvements.stream().distinct().limit(5).collect(Collectors.toList()))
                .recommendedTopics(List.of("System Design Deep Dive", "Advanced JVM Internals", "Distributed Transactions"))
                .questionSummaries(summaries)
                .build();
    }

    private CurrentQuestionResponse mapToCurrentQuestion(InterviewQuestion q, int total) {
        return CurrentQuestionResponse.builder()
                .questionId(q.getId())
                .questionNumber(q.getQuestionOrder())
                .totalQuestions(total)
                .topic(q.getTopic())
                .questionText(q.getQuestionText())
                .aiProbeHint(q.getAiProbeHint())
                .difficultyLabel(q.getDifficultyLabel())
                .rubricName(q.getRubricName())
                .build();
    }

    private List<String> splitToList(String text) {
        if (text == null || text.isBlank()) return List.of();
        return Arrays.stream(text.split("\n"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}