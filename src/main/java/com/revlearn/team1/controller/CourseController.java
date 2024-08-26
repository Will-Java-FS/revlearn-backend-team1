package com.revlearn.team1.controller;

import com.revlearn.team1.model.Course;
import com.revlearn.team1.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/{id}")
    public Course getCourseById(@RequestParam Long courseId) {
        return courseService.getById(courseId);
    }

}
