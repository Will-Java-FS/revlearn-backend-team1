package com.revlearn.team1.dto.course;

import com.revlearn.team1.enums.AttendanceMethod;

import java.time.LocalDate;

public record CourseDTO(Long id, Integer institutionId, LocalDate startDate, LocalDate endDate,
                        AttendanceMethod attendanceMethod, String name, String description
//        List<User> students,
//        List<User> educators
//                        Integer studentCnt, Integer educatorsCnt

) {
}
