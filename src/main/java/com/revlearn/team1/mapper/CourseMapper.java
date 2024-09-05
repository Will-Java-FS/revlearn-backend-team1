package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.course.CourseDTO;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.User;
import com.revlearn.team1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseMapper {
    private final UserRepository userRepository;

    //Does not return students or educators.
    //That is a separate method
    public CourseDTO toDto(Course course) {
        return new CourseDTO(course.getId(),
                course.getStartDate(),
                course.getEndDate(),
                course.getAttendanceMethod(),
                course.getName(),
                course.getDescription(),
                course.getPrice()
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
        course.setPrice(courseDTO.price());

        // Does not set students or educators

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
        if (courseDTO.price() != null) course.setPrice(courseDTO.price());
    }
}
