package com.revlearn.team1.controller;

import com.revlearn.team1.dto.module.ModuleResDTO;
import com.revlearn.team1.dto.module.ModuleReqDTO;
import com.revlearn.team1.constants.AccessLevelDesc;
import com.revlearn.team1.model.Exam;
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
    @Operation(summary = "Get Module", description = AccessLevelDesc.ENROLLED_STUDENT + "To get more detailed information of the module use module/{moduleId}/pages and module/{moduleId}/exams.", tags = {"module"})
    public ModuleResDTO getModuleById(@PathVariable Long moduleId) {
        return moduleService.getModuleById(moduleId);
    }

    @PostMapping("/course/{courseId}")
    @Operation(summary = "Create Module", description = AccessLevelDesc.ASSIGNED_EDUCATOR + "Once a module is created, pages and exams can be added to it through their respective controllers.", tags = {"module"})
    public ModuleResDTO createModule(@PathVariable Long courseId, @RequestBody ModuleReqDTO moduleReqDTO) {
        return moduleService.createModule(courseId, moduleReqDTO);
    }

    @PutMapping("/{moduleId}/course/{courseId}")
    @Operation(summary = "Update Module", description = AccessLevelDesc.ASSIGNED_EDUCATOR, tags = {"module"})
    public ModuleResDTO updateModule(@PathVariable Long moduleId, @RequestBody ModuleReqDTO moduleReqDTO) {
        return moduleService.updateModule(moduleId, moduleReqDTO);
    }

    @DeleteMapping("{moduleId}")
    @Operation(summary = "Delete Module", description = AccessLevelDesc.ASSIGNED_EDUCATOR, tags = {"module"})
    public String deleteModule(@PathVariable Long moduleId) {
        return moduleService.deleteModule(moduleId);
    }

    @GetMapping("/{moduleId}/pages")
    @Operation(summary = "Get All Pages of a Module", description = AccessLevelDesc.ENROLLED_STUDENT, tags = {"module"})
    public List<ModulePage> getModulePages(@PathVariable Long moduleId) {
        return moduleService.getModulePages(moduleId);
    }

    @GetMapping("/{moduleId}/exams")
    @Operation(summary = "Get All Exams of a Module", description = AccessLevelDesc.ENROLLED_STUDENT, tags = {"module"})
    public List<Exam> getExams(@PathVariable Long moduleId) {
        return moduleService.getExams(moduleId);
    }
}