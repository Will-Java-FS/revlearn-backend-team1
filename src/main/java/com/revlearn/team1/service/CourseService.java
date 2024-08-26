package com.revlearn.team1.service;

import com.revlearn.team1.exceptions.CourseNotFoundException;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.repository.CourseRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {
    CourseRepo courseRepo;

    public Course getById(Long courseId) {
        return courseRepo.findById(courseId).orElseThrow(
                () -> new CourseNotFoundException(String.format("Could not find course by Id in Database.  Course ID: %d", courseId))
        );
    }
}
