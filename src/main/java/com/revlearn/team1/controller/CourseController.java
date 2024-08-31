package com.revlearn.team1.controller;

import com.revlearn.team1.dto.CourseDTO;
import com.revlearn.team1.dto.request.CourseEducatorDTO;
import com.revlearn.team1.dto.request.CourseStudentDTO;
import com.revlearn.team1.dto.response.CourseEducatorResDTO;
import com.revlearn.team1.dto.response.CourseStudentResDTO;
import com.revlearn.team1.model.User;
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


    @GetMapping("/{courseId}/allStudents")
    public List<User> getAllStudentsByCourseId(@PathVariable Long courseId) {
        return courseService.getAllStudentsByCourseId(courseId);
    }

    @GetMapping("/{courseId}/allEducators")
    public List<User> getAllEducatorsByCourseId(@PathVariable Long courseId) {
        return courseService.getAllEducatorsByCourseId(courseId);
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

    //TODO: Hand off the responsibility of these three functions to User controller
//    @GetMapping("/student/{id}")
//    public List<CourseDTO> getCoursesOfStudent(@PathVariable Long id) {
//        return courseService.getAllByStudentId(id);
//    }
//
//    @GetMapping("/educator/{id}")
//    public List<CourseDTO> getCoursesOfEducator(@PathVariable Long id) {
//        return courseService.getAllByEducatorId(id);
//    }
//
//    // Does not need security because an institution's course list should be
//    // publicly available
//    @GetMapping("/institution/{id}")
//    public List<CourseDTO> getCoursesOfInstitution(@PathVariable Long id) {
//        return courseService.getAllByInstitutionId(id);
//    }
}
