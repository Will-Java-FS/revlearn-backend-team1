package com.revlearn.team1.controller;

import com.revlearn.team1.dto.course.CourseDTO;
import com.revlearn.team1.dto.course.request.CourseEducatorDTO;
import com.revlearn.team1.dto.course.request.CourseStudentDTO;
import com.revlearn.team1.dto.course.response.CourseEducatorResDTO;
import com.revlearn.team1.dto.course.response.CourseStudentResDTO;
import com.revlearn.team1.model.User;
import com.revlearn.team1.service.course.CourseServiceImp;
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

    @GetMapping("/{courseId}/allStudents")
    public List<User> getAllStudentsOfCourseId(@PathVariable Long courseId) {
        return courseService.getAllStudentsOfCourseId(courseId);
    }

    @GetMapping("/{courseId}/allEducators")
    public List<User> getAllEducatorsOfCourseId(@PathVariable Long courseId) {
        return courseService.getAllEducatorsOfCourseId(courseId);
    }

    @GetMapping("/{progress_id}")
    public CourseDTO getCourseById(@PathVariable Long id) {
        return courseService.getById(id);
    }


    @PostMapping("/create")//TODO: Secure so that only instructors and institutions can create courses
    public CourseDTO postCourse(@RequestBody CourseDTO courseDTO) {
        return courseService.createCourse(courseDTO);
    }

    @PutMapping("/update")
    public CourseDTO updateCourse(@RequestBody CourseDTO courseDTO) {
        //TODO: Secure so only educators and institution roles can access.  Further security in service layer.
        return courseService.updateCourse(courseDTO);
    }

    @DeleteMapping("/delete/{id}")
    //TODO: Secure so that only course owners (instructors and institutions) can delete courses
    public String deleteCourse(@PathVariable Long id) {
        return courseService.deleteById(id);
    }

    @PatchMapping("/studentEnroll")
    public CourseStudentResDTO enrollStudent(@RequestBody CourseStudentDTO courseStudentDTO) {
        return courseService.enrollStudent(courseStudentDTO);
    }

    @PatchMapping("/studentWithdraw")
    public CourseStudentResDTO withdrawStudent(@RequestBody CourseStudentDTO courseStudentDTO) {
        return courseService.withdrawStudent(courseStudentDTO);
    }

    @PatchMapping("/educatorAdd")
    public CourseEducatorResDTO addEducator(@RequestBody CourseEducatorDTO courseEducatorDTO) {
        //TODO: Secure so only educators and institution roles can access.  Further security logic in service layer.
        return courseService.addEducator(courseEducatorDTO);
    }

    @PatchMapping("/educatorRemove")
    public CourseEducatorResDTO removeEducator(@RequestBody CourseEducatorDTO courseEducatorDTO) {
        //TODO: Secure so only educators and institution roles can access.  Further security logic in service layer.
        return courseService.removeEducator(courseEducatorDTO);
    }
}
