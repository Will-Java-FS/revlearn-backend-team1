package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.course.CourseDTO;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.User;
import com.revlearn.team1.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseMapper {
    private final UserService userService;

    //Does not return students or educators.
    //That is a separate method
    public CourseDTO toDto(Course course) {
        return new CourseDTO(course.getId(),
                course.getInstitution().getId(),
                course.getStartDate(),
                course.getEndDate(),
                course.getAttendanceMethod(),
                course.getName(),
                course.getDescription()
        );
    }

    public Course fromDto(CourseDTO courseDTO) {
        Course course = new Course();

        //TODO consider different DTO structures
        //Should not set ID because it is auto-generated
//        course.setId(courseDTO.id());

        course.setStartDate(courseDTO.startDate());
        course.setEndDate(courseDTO.endDate());
        course.setAttendanceMethod(courseDTO.attendanceMethod());
        course.setName(courseDTO.name());
        course.setDescription(courseDTO.description());

        // Fetch institution user from userService
        if (courseDTO.institutionId() != null) {
            User institution = userService.findById(Math.toIntExact(courseDTO.institutionId()))
                    .orElseThrow(
                            () -> new IllegalArgumentException("User not found with ID: " + courseDTO.institutionId()));
            course.setInstitution(institution);
        }

        //There are no students or educators included because that should be handled elsewhere
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
        //Does not affect student list or educators list.  Those are handled separately.

        if (courseDTO.startDate() != null) course.setStartDate(courseDTO.startDate());
        if (courseDTO.endDate() != null) course.setEndDate(courseDTO.endDate());
        if (courseDTO.name() != null) course.setName(courseDTO.name());
        if (courseDTO.description() != null) course.setDescription(courseDTO.description());
        if (courseDTO.attendanceMethod() != null) course.setAttendanceMethod(courseDTO.attendanceMethod());

    }
}
