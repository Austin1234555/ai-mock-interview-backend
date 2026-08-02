//package com.Austin.mockinterview.dto.interview;
//
//import java.util.List;
//import java.util.Map;
//
//public class InterviewOptionsResponse {
//    private List<String> jobRoles;
//    private List<String> experienceTiers;
//    private List<String> difficultyLevels;
//    private List<Integer> durations;          // minutes
//    private List<Integer> questionCounts;
//    private List<String> suggestedDomains;    // optional suggestions
//
//    // constructors + getters + setters (or use Lombok @Data)
//}



package com.Austin.mockinterview.dto.interview;

import lombok.Data;
import java.util.List;

@Data
public class InterviewOptionsResponse {
    private List<String> jobRoles;
    private List<String> experienceTiers;
    private List<String> difficultyLevels;
    private List<Integer> durations;
    private List<Integer> questionCounts;
    private List<String> suggestedDomains;
}