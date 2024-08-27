package com.revlearn.team1.mapper;

import java.util.HashSet;

import org.springframework.stereotype.Component;

import com.revlearn.team1.dto.CourseDTO;
import com.revlearn.team1.model.Course;

@Component
public class CourseMapper {

    // Converts from Course entity to CourseDTO
    public CourseDTO toDto(Course course) {
        return new CourseDTO(
                course.getInstitution(),
                course.getStartDate(),
                course.getEndDate(),
                course.getType(),
                course.getName(),
                course.getDescription(),
                course.getStudents(),
                course.getEducators()
        );
    }

    // Converts from CourseDTO to Course entity
    public Course fromDto(CourseDTO courseDTO) {
        Course course = new Course();
        course.setInstitution(courseDTO.institution());
        course.setStartDate(courseDTO.startDate());
        course.setEndDate(courseDTO.endDate());
        course.setType(courseDTO.type());
        course.setName(courseDTO.name());
        course.setDescription(courseDTO.description());

        // Set students and educators
        if (courseDTO.students() != null) {
            course.getStudents().addAll(courseDTO.students());
        } else {
            course.setStudents(new HashSet<>()); // Optional, ensures an empty set
        }

        if (courseDTO.educators() != null) {
            course.getEducators().addAll(courseDTO.educators());
        } else {
            course.setEducators(new HashSet<>()); // Optional, ensures an empty set
        }

        return course;
    }
}
