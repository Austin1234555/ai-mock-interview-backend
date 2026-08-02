//package com.Austin.mockinterview.dto.interview;
//
//import com.Austin.mockinterview.enums.DifficultyLevel;
//import com.Austin.mockinterview.enums.ExperienceTier;
//import com.Austin.mockinterview.enums.JobRole;
//import jakarta.validation.constraints.NotNull;
//
//public class CreateInterviewConfigRequest {
//
//    @NotNull
//    private JobRole jobRole;
//
//    @NotNull
//    private ExperienceTier experienceTier;
//
//    @NotNull
//    private DifficultyLevel difficultyLevel;
//
//    @NotNull
//    private Integer durationMinutes;     // 15, 30, 45, 60
//
//    @NotNull
//    private Integer numberOfQuestions;   // 5, 10, 15, 20
//
//    private String coreDomainFocus;      // free text
//
//    private boolean includeCodingChallenge;
//
//    // getters + setters
//}



package com.Austin.mockinterview.dto.interview;

import com.Austin.mockinterview.enums.DifficultyLevel;
import com.Austin.mockinterview.enums.ExperienceTier;
import com.Austin.mockinterview.enums.JobRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateInterviewConfigRequest {

    @NotNull
    private JobRole jobRole;

    @NotNull
    private ExperienceTier experienceTier;

    @NotNull
    private DifficultyLevel difficultyLevel;

    @NotNull
    private Integer durationMinutes;

    @NotNull
    private Integer numberOfQuestions;

    private String coreDomainFocus;

    private boolean includeCodingChallenge;
}