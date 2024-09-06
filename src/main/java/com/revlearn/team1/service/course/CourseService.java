package com.revlearn.team1.service.course;

import com.revlearn.team1.dto.course.CourseDTO;
import com.revlearn.team1.dto.course.request.CourseEducatorDTO;
import com.revlearn.team1.dto.course.request.CourseStudentDTO;
import com.revlearn.team1.dto.course.response.CourseEducatorResDTO;
import com.revlearn.team1.dto.course.response.CourseStudentResDTO;
import com.revlearn.team1.dto.module.ModuleDTO;
import com.revlearn.team1.model.User;

import java.util.List;

public interface CourseService {
    List<CourseDTO> getAll();

    List<User> getAllStudentsOfCourseId(Long courseId);

    List<User> getAllEducatorsOfCourseId(Long courseId);

    CourseDTO getById(Long courseId);

    CourseDTO createCourse(CourseDTO courseDTO);

    CourseDTO updateCourse(CourseDTO courseDTO);

    String deleteById(Long id);

    CourseEducatorResDTO addEducator(CourseEducatorDTO courseEducatorDTO);

    CourseEducatorResDTO removeEducator(CourseEducatorDTO courseEducatorDTO);

    CourseStudentResDTO enrollStudent(CourseStudentDTO courseStudentDTO);

    CourseStudentResDTO withdrawStudent(CourseStudentDTO courseStudentDTO);

    List<ModuleDTO> getModulesByCourseId(Long courseId);


}
