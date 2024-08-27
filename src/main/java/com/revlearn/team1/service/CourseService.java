package com.revlearn.team1.service;

import com.revlearn.team1.dto.CourseDTO;
import com.revlearn.team1.dto.request.CourseEducatorDTO;
import com.revlearn.team1.dto.response.CourseEducatorResDTO;

import java.util.List;

public interface CourseService {
    public List<CourseDTO> getAll();

    public CourseDTO getById(Long courseId);

    public CourseDTO createCourse(CourseDTO courseDTO);

    public CourseDTO updateCourse(CourseDTO courseDTO);

    public CourseEducatorResDTO addEducator(CourseEducatorDTO courseEducatorDTO);

    public CourseEducatorResDTO removeEducator(CourseEducatorDTO courseEducatorDTO);

}
