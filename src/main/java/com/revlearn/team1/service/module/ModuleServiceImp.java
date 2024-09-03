package com.revlearn.team1.service.module;

import com.revlearn.team1.dto.course.ModuleDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModuleServiceImp implements ModuleService {
    @Override
    public List<ModuleDTO> getModulesByCourseId(Long courseId) {
        return null;
    }

    @Override
    public ModuleDTO getModuleById(Long moduleId) {
        return null;
    }

    @Override
    public ModuleDTO createModule(ModuleDTO moduleDTO) {
        //TODO: implement static set in course to ensure uniqueness of CourseModule orderIndexes
//         = (long) course.getCourseModules().size();
        return null;
    }

    @Override
    public ModuleDTO updateModule(Long moduleId, ModuleDTO moduleDTO) {
        return null;
    }

    @Override
    public String deleteModule(Long moduleId) {
        return null;
    }
}
