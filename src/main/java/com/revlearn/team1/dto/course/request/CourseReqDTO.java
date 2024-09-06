package com.revlearn.team1.dto.course.request;

import com.revlearn.team1.enums.AttendanceMethod;

import java.time.LocalDate;

public record CourseReqDTO(LocalDate startDate, LocalDate endDate,
                           AttendanceMethod attendanceMethod, String name, String description, Float price
) {
}
