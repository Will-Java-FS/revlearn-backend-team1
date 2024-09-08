package com.revlearn.team1.dto.module;

public record ModuleResDTO(
        Long id,
        String title,
        String description,
        Long orderIndex,
        Long courseId
) {
}
