package com.revlearn.team1.dto;

import java.time.LocalDate;
import java.util.Set;

import com.revlearn.team1.enums.AttendanceMethod;

public record CourseDTO(
        Long id,
        String name,
        String description,
        Long institutionId, 
        LocalDate startDate,
        LocalDate endDate,
        AttendanceMethod type,
        Set<Long> educatorIds, 
        Set<Long> studentIds 
) {
}
