package com.revlearn.team1.service;

import com.revlearn.team1.dto.CourseDTO;
import com.revlearn.team1.exceptions.CourseNotFoundException;
import com.revlearn.team1.mapper.CourseMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.repository.CourseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepo courseRepo;
    private final CourseMapper courseMapper;

    public CourseDTO getById(Long courseId) {
        Course retrievedCourse = courseRepo.findById(courseId).orElseThrow(
                () -> new CourseNotFoundException(String.format("Could not find course by Id in Database.  Course ID: %d", courseId))
        );
        return courseMapper.toDto(retrievedCourse);
    }

    public CourseDTO createCourse(CourseDTO courseDTO) {
        Course course = courseMapper.fromDto(courseDTO);
        Course savedCourse = courseRepo.save(course);
        return courseMapper.toDto(savedCourse);
    }
}
