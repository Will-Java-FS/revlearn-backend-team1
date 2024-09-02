package com.revlearn.team1.dto;

import com.revlearn.team1.enums.AttendanceMethod;
import com.revlearn.team1.model.User;

import java.time.LocalDate;
import java.util.List;

public record CourseDTO(
        Long id,
        Long institutionId,
        LocalDate startDate,
        LocalDate endDate,
        AttendanceMethod attendanceMethod,
        String name,
        String description,
        List<Long> studentsIds,
        List<Long> educatorsIds

) {
}
