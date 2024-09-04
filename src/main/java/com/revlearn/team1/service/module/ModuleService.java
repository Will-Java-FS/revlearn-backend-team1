package com.revlearn.team1.service.module;

import com.revlearn.team1.dto.module.ModuleDTO;
import com.revlearn.team1.model.ModulePage;

import java.util.List;

public interface ModuleService {

    ModuleDTO getModuleById(Long moduleId);

    ModuleDTO createModule(ModuleDTO moduleDTO);

    ModuleDTO updateModule(Long moduleId, ModuleDTO moduleDTO);

    String deleteModule(Long moduleId);

    List<ModulePage> getModulePages(Long moduleId);
}
