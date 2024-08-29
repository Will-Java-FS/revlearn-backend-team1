package com.revlearn.team1.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.revlearn.team1.dto.CourseDTO;
import com.revlearn.team1.model.Course;

@Component
public class CourseMapper {

    // Converts from Course entity to CourseDTO
    public CourseDTO toDto(Course course) {
        return new CourseDTO(
                course.getId(),
                course.getInstitution(),
                course.getStartDate(),
                course.getEndDate(),
                course.getAttendanceMethod(),
                course.getName(),
                course.getDescription(),
                course.getStudents(),
                course.getEducators()
        );
    }

    // Converts from CourseDTO to Course entity
    public Course fromDto(CourseDTO courseDTO) {
        Course course = new Course();
        course.setId(courseDTO.id());
        course.setInstitution(courseDTO.institution());
        course.setStartDate(courseDTO.startDate());
        course.setEndDate(courseDTO.endDate());
        course.setAttendanceMethod(courseDTO.attendanceMethod());
        course.setName(courseDTO.name());
        course.setDescription(courseDTO.description());

        // Set students and educators
        if (courseDTO.students() != null) {
            course.getStudents().addAll(courseDTO.students());
        } else {
            course.setStudents(new ArrayList<>()); // Optional, ensures an empty set
        }

        if (courseDTO.educators() != null) {
            course.getEducators().addAll(courseDTO.educators());
        } else {
            course.setEducators(new ArrayList<>()); // Optional, ensures an empty set
        }

        return course;
    }

    public void updateCourseFromDto(Course course, CourseDTO courseDTO) {
        //Only meant for educators or institutions to use

        if (courseDTO.startDate() != null) course.setStartDate(courseDTO.startDate());
        if (courseDTO.endDate() != null) course.setEndDate(courseDTO.endDate());
        if (courseDTO.name() != null) course.setName(courseDTO.name());
        if (courseDTO.description() != null) course.setDescription(courseDTO.description());
        if (courseDTO.attendanceMethod() != null) course.setAttendanceMethod(courseDTO.attendanceMethod());

    }
}
