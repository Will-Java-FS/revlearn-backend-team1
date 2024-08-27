package com.revlearn.team1.controller;

import com.revlearn.team1.dto.CourseDTO;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/{id}")
    public CourseDTO getCourseById(@PathVariable Long id) {
        return courseService.getById(id);
    }

    @PostMapping()//This should be secured so that only admins can create courses
    public CourseDTO postCourse(@RequestBody CourseDTO courseDTO){
        return courseService.createCourse(courseDTO);
    }
}
