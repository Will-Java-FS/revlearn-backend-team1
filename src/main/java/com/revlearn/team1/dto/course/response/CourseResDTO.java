package com.revlearn.team1.dto.course.response;

import com.revlearn.team1.dto.user.UserResDTO;
import com.revlearn.team1.enums.AttendanceMethod;

import java.time.LocalDate;
import java.util.List;

public record CourseResDTO(Long id, LocalDate startDate, LocalDate endDate,
                           AttendanceMethod attendanceMethod, String name, String description, Float price, List<UserResDTO> educators
) {
}
