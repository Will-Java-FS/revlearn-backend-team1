package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.course.request.CourseReqDTO;
import com.revlearn.team1.dto.course.response.CourseResDTO;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CourseMapper {
    private final UserRepository userRepository;

    //These methods do not return or set students or educators.
    //That is a separate method

    public CourseResDTO toDto(Course course) {
        return new CourseResDTO(
                course.getId(),
                course.getStartDate(),
                course.getEndDate(),
                course.getAttendanceMethod(),
                course.getName(),
                course.getDescription(),
                course.getPrice()
        );
    }

    public Course fromReqDto(CourseReqDTO courseReqDTO) {
        Course course = new Course();

        course.setStartDate(courseReqDTO.startDate());
        course.setEndDate(courseReqDTO.endDate());
        course.setAttendanceMethod(courseReqDTO.attendanceMethod());
        course.setName(courseReqDTO.name());
        course.setDescription(courseReqDTO.description());
        course.setPrice(courseReqDTO.price());

        return course;
    }

    public void updateCourseFromReqDto(Course course, CourseReqDTO courseReqDTO) {

        if (courseReqDTO.startDate() != null) course.setStartDate(courseReqDTO.startDate());
        if (courseReqDTO.endDate() != null) course.setEndDate(courseReqDTO.endDate());
        if (courseReqDTO.name() != null) course.setName(courseReqDTO.name());
        if (courseReqDTO.description() != null) course.setDescription(courseReqDTO.description());
        if (courseReqDTO.attendanceMethod() != null) course.setAttendanceMethod(courseReqDTO.attendanceMethod());
        if (courseReqDTO.price() != null) course.setPrice(courseReqDTO.price());
    }
}
