package com.revlearn.team1.dto.exam;

import com.revlearn.team1.dto.examquestion.ExamQuestionReqDTO;

import java.util.List;

public record ExamReqDTO(
    String title,
    String description,
    String instructions,
    Long duration,
    String type,
    List<ExamQuestionReqDTO> questions
) {
}
