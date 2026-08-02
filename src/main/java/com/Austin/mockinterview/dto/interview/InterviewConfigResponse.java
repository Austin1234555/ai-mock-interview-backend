//package com.Austin.mockinterview.dto.interview;
//
//import com.Austin.mockinterview.enums.DifficultyLevel;
//import com.Austin.mockinterview.enums.ExperienceTier;
//import com.Austin.mockinterview.enums.JobRole;
//import java.time.LocalDateTime;
//
//public class InterviewConfigResponse {
//
//    private Long id;
//    private JobRole jobRole;
//    private ExperienceTier experienceTier;
//    private DifficultyLevel difficultyLevel;
//    private Integer durationMinutes;
//    private Integer numberOfQuestions;
//    private String coreDomainFocus;
//    private boolean includeCodingChallenge;
//    private LocalDateTime createdAt;
//
//    // ===== Getters & Setters =====
//    // (or use Lombok @Data)
//}


package com.Austin.mockinterview.dto.interview;

import com.Austin.mockinterview.enums.DifficultyLevel;
import com.Austin.mockinterview.enums.ExperienceTier;
import com.Austin.mockinterview.enums.JobRole;
import java.time.LocalDateTime;

public class InterviewConfigResponse {

    private Long id;
    private JobRole jobRole;
    private ExperienceTier experienceTier;
    private DifficultyLevel difficultyLevel;
    private Integer durationMinutes;
    private Integer numberOfQuestions;
    private String coreDomainFocus;
    private boolean includeCodingChallenge;
    private LocalDateTime createdAt;

    // ===== Getters =====
    public Long getId() {
        return id;
    }

    public JobRole getJobRole() {
        return jobRole;
    }

    public ExperienceTier getExperienceTier() {
        return experienceTier;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public Integer getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public String getCoreDomainFocus() {
        return coreDomainFocus;
    }

    public boolean isIncludeCodingChallenge() {
        return includeCodingChallenge;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // ===== Setters =====
    public void setId(Long id) {
        this.id = id;
    }

    public void setJobRole(JobRole jobRole) {
        this.jobRole = jobRole;
    }

    public void setExperienceTier(ExperienceTier experienceTier) {
        this.experienceTier = experienceTier;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public void setNumberOfQuestions(Integer numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }

    public void setCoreDomainFocus(String coreDomainFocus) {
        this.coreDomainFocus = coreDomainFocus;
    }

    public void setIncludeCodingChallenge(boolean includeCodingChallenge) {
        this.includeCodingChallenge = includeCodingChallenge;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}