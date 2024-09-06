package com.revlearn.team1.controller;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.course.response.CourseResDTO;
import com.revlearn.team1.dto.program.ProgramReqDTO;
import com.revlearn.team1.dto.program.ProgramResDTO;
import com.revlearn.team1.dto.user.UserResDTO;
import com.revlearn.team1.service.program.ProgramService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/program")
@RequiredArgsConstructor
public class ProgramController {
    private final ProgramService programService;

    @GetMapping
    @Operation(summary = "Get All Programs", description = "Returns all programs.", tags = {"program"})
    public List<ProgramResDTO> getAllPrograms() {
        return programService.getAllPrograms();
    }

    @GetMapping("{programId}")
    @Operation(summary = "Get Program by Id", description = "", tags = {"program"})
    public ProgramResDTO getProgramById(@PathVariable Long programId) {
        return programService.getProgramById(programId);
    }

    @GetMapping("{programId}/courses")
    @Operation(summary = "Get Program Courses", description = "Returns all courses associated with a program.", tags = {"program"})
    public List<CourseResDTO> getProgramCourses(@PathVariable Long programId) {
        return programService.getProgramCourses(programId);
    }

    @GetMapping("{programId}/students")
    @Operation(summary = "Get Program Students", description = "Authorization required: institution (admin) account. Returns all students associated with a program.", tags = {"program"})
    public List<UserResDTO> getProgramStudents(@PathVariable Long programId) {
        return programService.getProgramStudents(programId);
    }

    @PostMapping
    @Operation(summary = "Create a Program", description = "Authorization required: institution (admin) account.", tags = {"program"})
    public ProgramResDTO postProgram(@RequestBody ProgramReqDTO programReqDTO) {
        return programService.createProgram(programReqDTO);
    }

    @PutMapping("{programId}")
    @Operation(summary = "Update a Program", description = "Authorization required: institution (admin) account.  Update an existing program.", tags = {"program"})
    public ProgramResDTO updateProgram(@PathVariable Long programId, @RequestBody ProgramReqDTO programReqDTO) {
        return programService.updateProgram(programId, programReqDTO);
    }

    @DeleteMapping("{programId}")
    @Operation(summary = "Delete a Program", description = "Authorization required: institution (admin) account.  Delete an existing program.", tags = {"program"})
    public MessageDTO deleteProgram(@PathVariable Long programId) {
        return programService.deleteProgram(programId);
    }

    @PatchMapping("{programId}/addCourse/{courseId}")
    @Operation(summary = "Add a Course to a Program", description = "Authorization required: institution (admin) account.  Add a course to a program.", tags = {"program"})
    public MessageDTO addCourseToProgram(@PathVariable Long programId, @PathVariable Long courseId) {
        return programService.addCourseToProgram(programId, courseId);
    }

    @PatchMapping("{programId}/removeCourse/{courseId}")
    @Operation(summary = "Remove a Course from a Program", description = "Authorization required: institution (admin) account.  Remove a course from a program.", tags = {"program"})
    public MessageDTO removeCourseFromProgram(@PathVariable Long programId, @PathVariable Long courseId) {
        return programService.removeCourseFromProgram(programId, courseId);
    }
}
