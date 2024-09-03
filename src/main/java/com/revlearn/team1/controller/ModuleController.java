package com.revlearn.team1.controller;

import com.revlearn.team1.dto.course.ModuleDTO;
import com.revlearn.team1.service.module.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/module")
@RequiredArgsConstructor
public class ModuleController {
    private final ModuleService moduleService;

    @GetMapping("/{courseId}/modules")
    public List<ModuleDTO> getModulesByCourseId(@PathVariable Long courseId) {
        //TODO: Secure so only course affiliated users can access (students, educators, & institution)
        return moduleService.getModulesByCourseId(courseId);
    }

    @GetMapping("/module/{moduleId}")
    public ModuleDTO getModuleById(@PathVariable Long moduleId) {
        //TODO: Secure so only course affiliated users can access (students, educators, & institution)
        return moduleService.getModuleById(moduleId);
    }

    @PostMapping("/module")
    public ModuleDTO createModule(@RequestBody ModuleDTO moduleDTO) {
        //TODO: Secure so only course owners (instructors and institutions) can create modules
        return moduleService.createModule(moduleDTO);
    }

    @PutMapping("/module/{moduleId}")
    public ModuleDTO updateModule(@PathVariable Long moduleId, @RequestBody ModuleDTO moduleDTO) {
        //TODO: Secure so only course owners (instructors and institutions) can update modules
        return moduleService.updateModule(moduleId, moduleDTO);
    }

    @DeleteMapping("/module/{moduleId}")
    public String deleteModule(@PathVariable Long moduleId) {
        //TODO: Secure so only course owners (instructors and institutions) can delete modules
        return moduleService.deleteModule(moduleId);
    }
}
