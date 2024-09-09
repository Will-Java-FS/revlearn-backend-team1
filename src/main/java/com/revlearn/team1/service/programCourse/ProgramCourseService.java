package com.revlearn.team1.service.programCourse;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.course.response.CourseResDTO;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.Program;

import java.util.List;

public interface ProgramCourseService {

    List<CourseResDTO> getProgramCourses(Program program);

    void removeAllCoursesFromProgram(Program program);
    void removeAllProgramsFromCourse(Course course);

    MessageDTO addCourseToProgram(Long programId, Long courseId);

    MessageDTO removeCourseFromProgram(Long programId, Long courseId);
}
