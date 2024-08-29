package com.revlearn.team1.service;

import com.revlearn.team1.dto.CourseDTO;
import com.revlearn.team1.dto.request.CourseEducatorDTO;
import com.revlearn.team1.dto.request.CourseStudentDTO;
import com.revlearn.team1.dto.response.CourseEducatorResDTO;
import com.revlearn.team1.dto.response.CourseStudentResDTO;

import java.util.List;

public interface CourseService {
    public List<CourseDTO> getAll();

    public CourseDTO getById(Long courseId);

    public CourseDTO createCourse(CourseDTO courseDTO);

    public CourseDTO updateCourse(CourseDTO courseDTO);

    public String deleteById(Long id);

    public List<CourseDTO> getAllByEducatorId(Long educatorId);

    public List<CourseDTO> getAllByInstitutionId(Long institutionId);

    public List<CourseDTO> getAllByStudentId(Long studentId);

    public CourseEducatorResDTO addEducator(CourseEducatorDTO courseEducatorDTO);

    public CourseEducatorResDTO removeEducator(CourseEducatorDTO courseEducatorDTO);

    public CourseStudentResDTO enrollStudent(CourseStudentDTO courseStudentDTO);

    public CourseStudentResDTO withdrawStudent(CourseStudentDTO courseStudentDTO);

}
