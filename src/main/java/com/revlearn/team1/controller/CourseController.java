package com.revlearn.team1.controller;

import com.revlearn.team1.dto.CourseDTO;
import com.revlearn.team1.dto.request.CourseEducatorDTO;
import com.revlearn.team1.dto.response.CourseEducatorResDTO;
import com.revlearn.team1.service.CourseServiceImp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseServiceImp courseService;

    @GetMapping("/all")
    public List<CourseDTO> getAllCourses() {
        return courseService.getAll();
    }

    @GetMapping("/{id}")
    public CourseDTO getCourseById(@PathVariable Long id) {
        return courseService.getById(id);
    }

    @PostMapping("/create")//TODO: Secure so that only instructors and institutions can create courses
    public CourseDTO postCourse(@RequestBody CourseDTO courseDTO) {
        return courseService.createCourse(courseDTO);
    }

    @PutMapping("/update")
    public CourseDTO updateCourse(@RequestBody CourseDTO courseDTO){
        //TODO: Secure so only educators and institution roles can access.  Further security in service layer.
        return courseService.updateCourse(courseDTO);
    }
    @PatchMapping("/addEducator")
    public CourseEducatorResDTO addEducator(@RequestBody CourseEducatorDTO courseEducatorDTO){
        //TODO: Secure so only educators and institution roles can access.  Further security logic in service layer.
        return courseService.addEducator(courseEducatorDTO);
    }

    @PatchMapping("/removeEducator")
    public CourseEducatorResDTO removeEducator(@RequestBody CourseEducatorDTO courseEducatorDTO){
        //TODO: Secure so only educators and institution roles can access.  Further security logic in service layer.
        return courseService.removeEducator(courseEducatorDTO);
    }
}
