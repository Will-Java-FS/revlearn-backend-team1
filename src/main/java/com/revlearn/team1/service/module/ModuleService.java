package com.revlearn.team1.service.module;

import com.revlearn.team1.dto.module.ModuleDTO;

public interface ModuleService {

    ModuleDTO getModuleById(Long moduleId);

    ModuleDTO createModule(ModuleDTO moduleDTO);

    ModuleDTO updateModule(Long moduleId, ModuleDTO moduleDTO);

    String deleteModule(Long moduleId);
}
