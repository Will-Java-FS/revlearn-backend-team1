package com.revlearn.team1.dto.module;

public record ModuleDTO(
        Long id,
        String title,
        String description,
        Long orderIndex,
        Long courseId
) {
}
