package com.revlearn.team1.service.module;

import com.revlearn.team1.dto.course.ModuleDTO;

import java.util.List;

public interface ModuleService {
    List<ModuleDTO> getModulesByCourseId(Long courseId);

    ModuleDTO getModuleById(Long moduleId);

    ModuleDTO createModule(ModuleDTO moduleDTO);

    ModuleDTO updateModule(Long moduleId, ModuleDTO moduleDTO);

    String deleteModule(Long moduleId);
}
