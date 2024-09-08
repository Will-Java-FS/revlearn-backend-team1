package com.revlearn.team1.controller;

import com.revlearn.team1.dto.course.response.CourseResDTO;
import com.revlearn.team1.dto.course.request.CourseReqDTO;
import com.revlearn.team1.dto.course.request.CourseEducatorDTO;
import com.revlearn.team1.dto.course.request.CourseStudentDTO;
import com.revlearn.team1.dto.course.response.CourseEducatorResDTO;
import com.revlearn.team1.dto.course.response.CourseStudentResDTO;
import com.revlearn.team1.dto.module.ModuleResDTO;
import com.revlearn.team1.constants.AccessLevelDesc;
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
    public List<CourseResDTO> getAllCourses() {
        return courseService.getAll();
    }

    @GetMapping("/{courseId}/students")
    @Operation(summary = "Get All Students of Course", description = AccessLevelDesc.ENROLLED_STUDENT, tags = { "course" })
    public List<User> getAllStudentsOfCourseId(@PathVariable Long courseId) {
        return courseService.getAllStudentsOfCourseId(courseId);
    }

    @GetMapping("/{courseId}/educators")
    @Operation(summary = "Get All Educators of Course", description = AccessLevelDesc.ENROLLED_STUDENT, tags = { "course" })
    public List<User> getAllEducatorsOfCourseId(@PathVariable Long courseId) {
        return courseService.getAllEducatorsOfCourseId(courseId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Course by Id", description = "", tags = { "course" })
    public CourseResDTO getCourseById(@PathVariable Long id) {
        return courseService.getById(id);
    }


    @PostMapping
    @Operation(summary = "Create a Course", description = AccessLevelDesc.ANY_EDUCATOR, tags = { "course" })
    public CourseResDTO postCourse(@RequestBody CourseReqDTO courseReqDTO) {
        return courseService.createCourse(courseReqDTO);
    }

    @PutMapping("/{courseId}")
    @Operation(summary = "Update a Course", description = AccessLevelDesc.ASSIGNED_EDUCATOR, tags = { "course" })
    public CourseResDTO updateCourse(@PathVariable Long courseId, @RequestBody CourseReqDTO courseReqDTO) {
        //TODO: Secure so only educators and institution roles can access.  Further security in service layer.
        return courseService.updateCourse(courseId, courseReqDTO);
    }

    @DeleteMapping("/{courseId}")
    @Operation(summary = "Delete a Course", description = AccessLevelDesc.ASSIGNED_EDUCATOR, tags = { "course" })
    //TODO: Secure so that only course owners (instructors and institutions) can delete courses
    public String deleteCourse(@PathVariable Long courseId) {
        return courseService.deleteById(courseId);
    }

    @PatchMapping("/student/add")
    @Operation(summary = "Enroll a Student User into a Course", description = AccessLevelDesc.AFFECTED_STUDENT, tags = { "course" })
    public CourseStudentResDTO enrollStudent(@RequestBody CourseStudentDTO courseStudentDTO) {
        return courseService.enrollStudent(courseStudentDTO);
    }

    @PatchMapping("/student/remove")
    @Operation(summary = "Withdraw a Student User from a Course", description = AccessLevelDesc.AFFECTED_STUDENT, tags = { "course" })
    public CourseStudentResDTO withdrawStudent(@RequestBody CourseStudentDTO courseStudentDTO) {
        return courseService.withdrawStudent(courseStudentDTO);
    }

    @PatchMapping("/educator/add")
    @Operation(summary = "Add an Educator User to a Course", description = AccessLevelDesc.ASSIGNED_EDUCATOR, tags = { "course" })
    public CourseEducatorResDTO addEducator(@RequestBody CourseEducatorDTO courseEducatorDTO) {
        //TODO: Secure so only educators and institution roles can access.  Further security logic in service layer.
        return courseService.addEducator(courseEducatorDTO);
    }

    @PatchMapping("/educator/remove")
    @Operation(summary = "Remove an Educator User from a Course", description = AccessLevelDesc.ASSIGNED_EDUCATOR, tags = { "course" })
    public CourseEducatorResDTO removeEducator(@RequestBody CourseEducatorDTO courseEducatorDTO) {
        //TODO: Secure so only educators and institution roles can access.  Further security logic in service layer.
        return courseService.removeEducator(courseEducatorDTO);
    }

    @GetMapping("/{courseId}/modules")
    @Operation(summary = "Get All Modules of Course", description = AccessLevelDesc.ENROLLED_STUDENT, tags = { "course" })
    public List<ModuleResDTO> getModulesByCourseId(@PathVariable Long courseId) {
        //TODO: Secure so only course affiliated users can access (students, educators, & institution)
        return courseService.getModulesByCourseId(courseId);
    }
}
