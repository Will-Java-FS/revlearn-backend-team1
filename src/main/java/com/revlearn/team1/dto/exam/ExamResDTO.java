package com.revlearn.team1.dto.exam;

import com.revlearn.team1.dto.examquestion.ExamQuestionResDTO;

import java.time.LocalDateTime;
import java.util.List;

public record ExamResDTO(
    Long id,
    String title,
    String description,
    String instructions,
    Long duration,
    String type,
    Long moduleId,
    List<ExamQuestionResDTO> questions,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
