package com.revlearn.team1.service.course;

import com.revlearn.team1.dto.course.request.CourseEducatorDTO;
import com.revlearn.team1.dto.course.request.CourseReqDTO;
import com.revlearn.team1.dto.course.request.CourseStudentDTO;
import com.revlearn.team1.dto.course.response.CourseEducatorResDTO;
import com.revlearn.team1.dto.course.response.CourseResDTO;
import com.revlearn.team1.dto.course.response.CourseStudentResDTO;
import com.revlearn.team1.dto.module.ModuleDTO;
import com.revlearn.team1.model.User;

import java.util.List;

public interface CourseService {
    List<CourseResDTO> getAll();

    List<User> getAllStudentsOfCourseId(Long courseId);

    List<User> getAllEducatorsOfCourseId(Long courseId);

    CourseResDTO getById(Long courseId);

    CourseResDTO createCourse(CourseReqDTO courseReqDTO);

    CourseResDTO updateCourse(Long courseId, CourseReqDTO courseReqDTO);

    String deleteById(Long id);

    CourseEducatorResDTO addEducator(CourseEducatorDTO courseEducatorDTO);

    CourseEducatorResDTO removeEducator(CourseEducatorDTO courseEducatorDTO);

    CourseStudentResDTO enrollStudent(CourseStudentDTO courseStudentDTO);

    CourseStudentResDTO withdrawStudent(CourseStudentDTO courseStudentDTO);

    List<ModuleDTO> getModulesByCourseId(Long courseId);


}
