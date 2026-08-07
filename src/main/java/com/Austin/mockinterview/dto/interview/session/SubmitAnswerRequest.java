package com.Austin.mockinterview.dto.interview.session;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SubmitAnswerRequest {

    @NotBlank(message = "Answer cannot be empty")
    private String answer;
}