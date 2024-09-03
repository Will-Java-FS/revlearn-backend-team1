package com.revlearn.team1.dto.course;

public record ModuleDTO(
        Long id,
        String title,
        String description,
        Long orderIndex,
        Long courseId
) {
}
