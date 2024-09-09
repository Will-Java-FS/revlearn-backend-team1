package com.revlearn.team1.dto.exam;

import java.time.LocalDateTime;

public record ExamResDTO(
    Long id,
    String title,
    String description,
    String instructions,
    Long duration,
    String type,
    Long moduleId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}
