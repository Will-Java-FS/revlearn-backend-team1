package com.revlearn.team1.dto.exam;

public record ExamReqDTO(
    String title,
    String description,
    String instructions,
    Long duration,
    String type
) {
}
