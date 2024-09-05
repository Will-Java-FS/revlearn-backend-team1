package com.revlearn.team1.controller;

import com.revlearn.team1.dto.module.ModuleDTO;
import com.revlearn.team1.model.ModulePage;
import com.revlearn.team1.service.module.ModuleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/module")
@RequiredArgsConstructor
public class ModuleController {
    private final ModuleService moduleService;

    @GetMapping("/{moduleId}")
    @Operation(summary = "Get Module", description = "Will require authenticated and authorized user (ie enrolled student, assigned educator, or owner institution).  Probably will not be used because course/{courseId}/modules can retrieve all of a course's modules.  To get more detailed information of the module use module/{moduleId}/pages and module/{moduleId}/exams.", tags = {"module"})
    public ModuleDTO getModuleById(@PathVariable Long moduleId) {
        //TODO: Secure so only course affiliated users can access (students, educators, & institution)
        return moduleService.getModuleById(moduleId);
    }

    @PostMapping
    @Operation(summary = "Create Module", description = "Will require authorized user (assigned educator or institution).  Once a module is created, pages and exams can be added to it.", tags = {"module"})
    public ModuleDTO createModule(@RequestBody ModuleDTO moduleDTO) {
        //TODO: Secure so only course owners (instructors and institutions) can create modules
        return moduleService.createModule(moduleDTO);
    }

    @PutMapping("/{moduleId}")
    @Operation(summary = "Update Module", description = "Will require authorized user (assigned educator or institution).", tags = {"module"})
    public ModuleDTO updateModule(@PathVariable Long moduleId, @RequestBody ModuleDTO moduleDTO) {
        //TODO: Secure so only course owners (instructors and institutions) can update modules
        return moduleService.updateModule(moduleId, moduleDTO);
    }

    @DeleteMapping("{moduleId}")
    @Operation(summary = "Delete Module", description = "Will require authorized user (assigned educator or institution).", tags = {"module"})
    public String deleteModule(@PathVariable Long moduleId) {
        //TODO: Secure so only course owners (instructors and institutions) can delete modules
        return moduleService.deleteModule(moduleId);
    }

    @GetMapping("/{moduleId}/pages")
    @Operation(summary = "Get All Pages of a Module", description = "Will require authenticated and authorized user (ie enrolled student, assigned educator, or owner institution).", tags = {"module"})
    public List<ModulePage> getModulePages(@PathVariable Long moduleId) {
        //TODO: Secure so only course affiliated users can access (enrolled students, assigned educators, & owner institution)
        return moduleService.getModulePages(moduleId);
    }

    //TODO:  Add endpoints to get all exams of a module
    //TODO:  Add endpoints to add and remove pages to/from a module
    //TODO: Add endpoints to add and remove exams to/from a module
}