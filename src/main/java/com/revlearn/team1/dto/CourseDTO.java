package com.revlearn.team1.dto;

import com.revlearn.team1.enums.AttendanceMethod;
import com.revlearn.team1.model.User;

import java.time.LocalDate;
import java.util.Set;

public record CourseDTO(
        User institution,
        LocalDate startDate,
        LocalDate endDate,
        AttendanceMethod attendanceMethod,
        String name,
        String description,
        Set<User> students,
        Set<User> educators

) {
}
