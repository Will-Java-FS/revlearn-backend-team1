package com.revlearn.team1.controller;

import com.revlearn.team1.dto.course.CourseDTO;
import com.revlearn.team1.dto.course.request.CourseEducatorDTO;
import com.revlearn.team1.dto.course.request.CourseStudentDTO;
import com.revlearn.team1.dto.course.response.CourseEducatorResDTO;
import com.revlearn.team1.dto.course.response.CourseStudentResDTO;
import com.revlearn.team1.dto.module.ModuleDTO;
import com.revlearn.team1.model.User;
import com.revlearn.team1.service.course.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping
    @Operation(summary = "Get All Courses", description = "Returns all courses.", tags = { "course" })
    public List<CourseDTO> getAllCourses() {
        return courseService.getAll();
    }

    @GetMapping("/{courseId}/students")
    @Operation(summary = "Get All Students of Course", description = "Requires authorization: enrolled student, assigned educator or institution (admin) account.", tags = { "course" })
    public List<User> getAllStudentsOfCourseId(@PathVariable Long courseId) {
        return courseService.getAllStudentsOfCourseId(courseId);
    }

    @GetMapping("/{courseId}/educators")
    @Operation(summary = "Get All Educators of Course", description = "Requires authorization: enrolled student, assigned educator or institution (admin) account.", tags = { "course" })
    public List<User> getAllEducatorsOfCourseId(@PathVariable Long courseId) {
        return courseService.getAllEducatorsOfCourseId(courseId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Course by Id", description = "", tags = { "course" })
    public CourseDTO getCourseById(@PathVariable Long id) {
        return courseService.getById(id);
    }


    @PostMapping
    @Operation(summary = "Create a Course", description = "Requires authorization: educator or institution (admin) account.", tags = { "course" })
    public CourseDTO postCourse(@RequestBody CourseDTO courseDTO) {
        //TODO: Secure so that only instructors and institutions can create courses
        return courseService.createCourse(courseDTO);
    }

    @PutMapping
    @Operation(summary = "Update a Course", description = "Requires authorization: assigned educator or institution (admin) account.", tags = { "course" })
    public CourseDTO updateCourse(@RequestBody CourseDTO courseDTO) {
        //TODO: Secure so only educators and institution roles can access.  Further security in service layer.
        return courseService.updateCourse(courseDTO);
    }

    @DeleteMapping("/{courseId}")
    @Operation(summary = "Delete a Course", description = "Requires authorization: assigned educator or institution (admin) account.", tags = { "course" })
    //TODO: Secure so that only course owners (instructors and institutions) can delete courses
    public String deleteCourse(@PathVariable Long courseId) {
        return courseService.deleteById(courseId);
    }

    @PatchMapping("/student/add")
    @Operation(summary = "Enroll a Student User into a Course", description = "Requires authorization: affected student, assigned educator, or institution (admin) account.", tags = { "course" })
    public CourseStudentResDTO enrollStudent(@RequestBody CourseStudentDTO courseStudentDTO) {
        return courseService.enrollStudent(courseStudentDTO);
    }

    @PatchMapping("/student/remove")
    @Operation(summary = "Withdraw a Student User from a Course", description = "Requires authorization: affected student, assigned educator, or institution (admin) account.", tags = { "course" })
    public CourseStudentResDTO withdrawStudent(@RequestBody CourseStudentDTO courseStudentDTO) {
        return courseService.withdrawStudent(courseStudentDTO);
    }

    @PatchMapping("/educator/add")
    @Operation(summary = "Add an Educator User to a Course", description = "Requires authorization: assigned educator or institution (admin) account.", tags = { "course" })
    public CourseEducatorResDTO addEducator(@RequestBody CourseEducatorDTO courseEducatorDTO) {
        //TODO: Secure so only educators and institution roles can access.  Further security logic in service layer.
        return courseService.addEducator(courseEducatorDTO);
    }

    @PatchMapping("/educator/remove")
    @Operation(summary = "Remove an Educator User from a Course", description = "Requires authorization: assigned educator or institution (admin) account.", tags = { "course" })
    public CourseEducatorResDTO removeEducator(@RequestBody CourseEducatorDTO courseEducatorDTO) {
        //TODO: Secure so only educators and institution roles can access.  Further security logic in service layer.
        return courseService.removeEducator(courseEducatorDTO);
    }

    @GetMapping("/{courseId}/modules")
    @Operation(summary = "Get All Modules of Course", description = "Requires authorization: enrolled student, assigned educator, or institution (admin) account.", tags = { "course" })
    public List<ModuleDTO> getModulesByCourseId(@PathVariable Long courseId) {
        //TODO: Secure so only course affiliated users can access (students, educators, & institution)
        return courseService.getModulesByCourseId(courseId);
    }
}
