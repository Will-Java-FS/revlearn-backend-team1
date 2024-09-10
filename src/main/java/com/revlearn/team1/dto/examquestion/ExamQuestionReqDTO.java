package com.revlearn.team1.dto.examquestion;

import java.util.List;

public record ExamQuestionReqDTO(
    String question,
    List<String> options,
    String answer
) {
}
