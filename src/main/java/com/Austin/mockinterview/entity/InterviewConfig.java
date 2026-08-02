//package com.Austin.mockinterview.entity;
//
//import com.Austin.mockinterview.enums.DifficultyLevel;
//import com.Austin.mockinterview.enums.ExperienceTier;
//import com.Austin.mockinterview.enums.JobRole;
//import jakarta.persistence.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "interview_configs")
//public class InterviewConfig {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private JobRole jobRole;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private ExperienceTier experienceTier;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private DifficultyLevel difficultyLevel;
//
//    @Column(nullable = false)
//    private Integer durationMinutes;
//
//    @Column(nullable = false)
//    private Integer numberOfQuestions;
//
//    private String coreDomainFocus;
//
//    @Column(nullable = false)
//    private boolean includeCodingChallenge = false;
//
//    @Column(nullable = false)
//    private LocalDateTime createdAt = LocalDateTime.now();
//
//    // ===== Getters & Setters =====
//    // (or use Lombok @Data + @NoArgsConstructor + @AllArgsConstructor)
//}



package com.Austin.mockinterview.entity;

import com.Austin.mockinterview.enums.DifficultyLevel;
import com.Austin.mockinterview.enums.ExperienceTier;
import com.Austin.mockinterview.enums.JobRole;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "interview_configs")
public class InterviewConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JobRole jobRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExperienceTier experienceTier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DifficultyLevel difficultyLevel;

    @Column(nullable = false)
    private Integer durationMinutes;

    @Column(nullable = false)
    private Integer numberOfQuestions;

    private String coreDomainFocus;

    @Column(nullable = false)
    private boolean includeCodingChallenge = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}