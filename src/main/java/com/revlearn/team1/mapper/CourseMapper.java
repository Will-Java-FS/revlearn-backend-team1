package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.CourseDTO;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.User;
import com.revlearn.team1.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseMapper {
    UserService userService;

    public CourseDTO toDto(Course course) {
        return new CourseDTO(course.getId(), course.getInstitution().getId(), course.getStartDate(), course.getEndDate(), course.getAttendanceMethod(), course.getName(), course.getDescription()
        );
    }

    public Course fromDto(CourseDTO courseDTO) {
        Course course = new Course();
        course.setId(courseDTO.id());
        course.setStartDate(courseDTO.startDate());
        course.setEndDate(courseDTO.endDate());
        course.setAttendanceMethod(courseDTO.attendanceMethod());
        course.setName(courseDTO.name());
        course.setDescription(courseDTO.description());

        // institution
        User institution = userService.findById(courseDTO.institutionId()).orElseThrow(() -> new RuntimeException(String.format("Failed to find insitution with id", courseDTO.institutionId())));
        course.setInstitution(institution);

        //There are no students or educators included because that should be handled elsewhere

        return course;
    }

    public void updateCourseFromDto(Course course, CourseDTO courseDTO) {
        //Only meant for educators or institutions to use
        //Does not affect student list or educators list.  Those are handled separately.

        if (courseDTO.startDate() != null) course.setStartDate(courseDTO.startDate());
        if (courseDTO.endDate() != null) course.setEndDate(courseDTO.endDate());
        if (courseDTO.name() != null) course.setName(courseDTO.name());
        if (courseDTO.description() != null) course.setDescription(courseDTO.description());
        if (courseDTO.attendanceMethod() != null) course.setAttendanceMethod(courseDTO.attendanceMethod());

    }
}
