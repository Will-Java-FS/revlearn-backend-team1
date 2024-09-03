package com.revlearn.team1.dto.course.response;

public record CourseStudentResDTO(
        String message,
        Long courseId,
        Long studentId
) {
}
