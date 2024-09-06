package com.revlearn.team1.dto.course.response;

import com.revlearn.team1.enums.AttendanceMethod;

import java.time.LocalDate;

public record CourseResDTO(Long id, LocalDate startDate, LocalDate endDate,
                           AttendanceMethod attendanceMethod, String name, String description, Float price
) {
}
