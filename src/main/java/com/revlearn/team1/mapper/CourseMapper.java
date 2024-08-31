package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.CourseDTO;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.User;
import com.revlearn.team1.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseMapper {

    @Autowired
    private UserService userService;

    // Converts from Course entity to CourseDTO
    public CourseDTO toDto(Course course) {
        List<Long> educatorIds = course.getEducators().stream()
                .map((educator) -> (long) educator.getId())
                .collect(Collectors.toList());

        List<Long> studentIds = course.getStudents().stream()
                .map((student) -> (long) student.getId())
                .collect(Collectors.toList());

        return new CourseDTO(
                course.getId(),
                course.getInstitution() != null ? (long) course.getInstitution().getId() : null,
                course.getStartDate(),
                course.getEndDate(),
                course.getAttendanceMethod(),
                course.getName(),
                course.getDescription(),
                studentIds,
                educatorIds
        );
    }

    // Converts from CourseDTO to Course entity
    public Course fromDto(CourseDTO courseDTO) {
        Course course = new Course();
        course.setId(courseDTO.id());
        course.setName(courseDTO.name());
        course.setStartDate(courseDTO.startDate());
        course.setEndDate(courseDTO.endDate());
        course.setAttendanceMethod(courseDTO.attendanceMethod());
        course.setDescription(courseDTO.description());

        // Fetch institution user from userService
        if (courseDTO.institutionId() != null) {
            User institution = userService.findById(Math.toIntExact(courseDTO.institutionId()))
                    .orElseThrow(
                            () -> new IllegalArgumentException("User not found with ID: " + courseDTO.institutionId()));
            course.setInstitution(institution);
        }

//        // Set students and educators
//        if (courseDTO.students() != null) {
//            course.getStudents().addAll(courseDTO.students());
//        } else {
//            course.setStudents(new ArrayList<>()); // Optional, ensures an empty set
//            // Fetch educator users from userService
//            Set<User> educators = new HashSet<>();
//            if (courseDTO.educatorIds() != null) {
//                for (Long educatorId : courseDTO.educatorIds()) {
//                    User educator = userService.findById(educatorId)
//                            .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + educatorId));
//                    educators.add(educator);
//                }
//            }
//            course.setEducators(educators);
//
//            if (courseDTO.educators() != null) {
//                course.getEducators().addAll(courseDTO.educators());
//            } else {
//                course.setEducators(new ArrayList<>()); // Optional, ensures an empty set
//                // Fetch student users from userService
//                Set<User> students = new HashSet<>();
//                if (courseDTO.studentIds() != null) {
//                    for (Long studentId : courseDTO.studentIds()) {
//                        User student = userService.findById(studentId)
//                                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + studentId));
//                        students.add(student);
//                    }
//                }
//                course.setStudents(students);

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
