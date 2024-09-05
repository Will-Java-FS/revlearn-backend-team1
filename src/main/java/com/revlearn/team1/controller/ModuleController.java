package com.revlearn.team1.controller;

import com.revlearn.team1.dto.module.ModuleDTO;
import com.revlearn.team1.model.Exam;
import com.revlearn.team1.model.ModulePage;
import com.revlearn.team1.service.module.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/module")
@RequiredArgsConstructor
public class ModuleController {
    private final ModuleService moduleService;

    @GetMapping("/{moduleId}")
    public ModuleDTO getModuleById(@PathVariable Long moduleId) {
        //TODO: Secure so only course affiliated users can access (students, educators, & institution)
        return moduleService.getModuleById(moduleId);
    }

    @PostMapping
    public ModuleDTO createModule(@RequestBody ModuleDTO moduleDTO) {
        //TODO: Secure so only course owners (instructors and institutions) can create modules
        return moduleService.createModule(moduleDTO);
    }

    @PutMapping("/{moduleId}")
    public ModuleDTO updateModule(@PathVariable Long moduleId, @RequestBody ModuleDTO moduleDTO) {
        //TODO: Secure so only course owners (instructors and institutions) can update modules
        return moduleService.updateModule(moduleId, moduleDTO);
    }

    @DeleteMapping("{moduleId}")
    public String deleteModule(@PathVariable Long moduleId) {
        //TODO: Secure so only course owners (instructors and institutions) can delete modules
        return moduleService.deleteModule(moduleId);
    }

    @GetMapping("/{moduleId}/pages")
    public List<ModulePage> getModulePages(@PathVariable Long moduleId) {
        return moduleService.getModulePages(moduleId);
    }

    @GetMapping("/{moduleId}/exams")
    public List<Exam> getExams(@PathVariable Long moduleId) {
        return moduleService.getExams(moduleId);
    }
}