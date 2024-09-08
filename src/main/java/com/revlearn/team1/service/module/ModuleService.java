package com.revlearn.team1.service.module;

import com.revlearn.team1.dto.module.ModuleResDTO;
import com.revlearn.team1.dto.module.ModuleReqDTO;
import com.revlearn.team1.model.Exam;
import com.revlearn.team1.model.Page;

import java.util.List;

public interface ModuleService {

    ModuleResDTO getModuleById(Long moduleId);

    ModuleResDTO createModule(Long CourseId, ModuleReqDTO moduleReqDTO);

    ModuleResDTO updateModule(Long moduleId, ModuleReqDTO moduleReqDTO);

    String deleteModule(Long moduleId);

    List<Page> getModulePages(Long moduleId);

    List<Exam> getExams(Long moduleId);
}
