package com.revlearn.team1.dto.examquestion;

import java.util.List;

public record ExamQuestionResDTO(
    Long id,
    String question,
    List<String> options,
    String answer,
    Long examId
) {
}