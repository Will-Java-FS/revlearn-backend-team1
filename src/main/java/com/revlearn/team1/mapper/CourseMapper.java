package com.revlearn.team1.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.revlearn.team1.dto.CourseDTO;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.User;
import com.revlearn.team1.service.UserService;

@Component
public class CourseMapper {

    @Autowired
    private UserService userService;

    // Converts from Course entity to CourseDTO
    public CourseDTO toDto(Course course) {
        Set<Long> educatorIds = course.getEducators().stream()
                .map(User::getId)
                .collect(Collectors.toSet());

        Set<Long> studentIds = course.getStudents().stream()
                .map(User::getId)
                .collect(Collectors.toSet());

        return new CourseDTO(
                course.getId(),
                        course.getName(),
                course.getDescription(),
                course.getInstitution() != null ? course.getInstitution().getId() : null,
                course.getStartDate(),
                course.getEndDate(),
                course.getType(),
                educatorIds,
                studentIds);
    }

    // Converts from CourseDTO to Course entity
    public Course fromDto(CourseDTO courseDTO) {
        Course course = new Course();
        course.setName(courseDTO.name());
        course.setDescription(courseDTO.description());
        course.setStartDate(courseDTO.startDate());
        course.setEndDate(courseDTO.endDate());
        course.setType(courseDTO.type());

        // Fetch institution user from userService
        if (courseDTO.institutionId() != null) {
            User institution = userService.findById(courseDTO.institutionId())
                    .orElseThrow(
                            () -> new IllegalArgumentException("User not found with ID: " + courseDTO.institutionId()));
            course.setInstitution(institution);
        }

        // Fetch educator users from userService
        Set<User> educators = new HashSet<>();
        if (courseDTO.educatorIds() != null) {
            for (Long educatorId : courseDTO.educatorIds()) {
                User educator = userService.findById(educatorId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + educatorId));
                educators.add(educator);
            }
        }
        course.setEducators(educators);

        // Fetch student users from userService
        Set<User> students = new HashSet<>();
        if (courseDTO.studentIds() != null) {
            for (Long studentId : courseDTO.studentIds()) {
                User student = userService.findById(studentId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + studentId));
                students.add(student);
            }
        }
        course.setStudents(students);

        return course;
    }
}
