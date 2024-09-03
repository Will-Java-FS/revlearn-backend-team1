package com.revlearn.team1.service.course;

import com.revlearn.team1.dto.course.CourseDTO;
import com.revlearn.team1.dto.course.ModuleDTO;
import com.revlearn.team1.dto.course.request.CourseEducatorDTO;
import com.revlearn.team1.dto.course.request.CourseStudentDTO;
import com.revlearn.team1.dto.course.response.CourseEducatorResDTO;
import com.revlearn.team1.dto.course.response.CourseStudentResDTO;
import com.revlearn.team1.model.User;

import java.util.List;

public interface CourseService {
    public List<CourseDTO> getAll();

    List<User> getAllStudentsOfCourseId(Long courseId);

    List<User> getAllEducatorsOfCourseId(Long courseId);

    public CourseDTO getById(Long courseId);

    public CourseDTO createCourse(CourseDTO courseDTO);

    public CourseDTO updateCourse(CourseDTO courseDTO);

    public String deleteById(Long id);

    public CourseEducatorResDTO addEducator(CourseEducatorDTO courseEducatorDTO);

    public CourseEducatorResDTO removeEducator(CourseEducatorDTO courseEducatorDTO);

    public CourseStudentResDTO enrollStudent(CourseStudentDTO courseStudentDTO);

    public CourseStudentResDTO withdrawStudent(CourseStudentDTO courseStudentDTO);

    public List<ModuleDTO> getModulesByCourseId(Long courseId);


}
