package com.revlearn.team1.service.module;

import com.revlearn.team1.dto.course.ModuleDTO;
import com.revlearn.team1.exceptions.ModuleNotFoundException;
import com.revlearn.team1.exceptions.ServiceLayerDataAccessException;
import com.revlearn.team1.mapper.ModuleMapper;
import com.revlearn.team1.model.CourseModule;
import com.revlearn.team1.repository.ModuleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModuleServiceImp implements ModuleService {
    private final ModuleRepo moduleRepo;
    private final ModuleMapper moduleMapper;

    @Override
    public ModuleDTO getModuleById(Long moduleId) {
        //TODO: Secure so only course affiliated users can access (enrolled students, assigned educators, & institution)
        return moduleRepo.findById(moduleId).map(moduleMapper::toDto).orElseThrow(
                () -> new ModuleNotFoundException(moduleId));
    }

    @Override
    public ModuleDTO createModule(ModuleDTO moduleDTO) {
        CourseModule courseModule = moduleMapper.toEntity(moduleDTO);
        //TODO: Secure so only course owners can create (assigned educators, & institution)

        //default orderIndex to last in list
        courseModule.setOrderIndex((long) courseModule.getCourse().getCourseModules().size());

        try {
            return moduleMapper.toDto(moduleRepo.save(courseModule));
        } catch (Exception e) {
            throw new ServiceLayerDataAccessException("Failed to create module", e);
        }
    }

    @Override
    public ModuleDTO updateModule(Long moduleId, ModuleDTO moduleDTO) {
        //TODO: Secure so only course owners (instructors and institutions) can update modules
        //TODO: implement static set in course to ensure uniqueness of CourseModule orderIndexes
        //Verify module exists
        CourseModule courseModule = moduleRepo.findById(moduleId).orElseThrow(
                () -> new ModuleNotFoundException(moduleId));
        //Update module
        moduleMapper.updateEntityFromDto(courseModule, moduleDTO);
        try {
            return moduleMapper.toDto(moduleRepo.save(courseModule));
        } catch (Exception e) {
            throw new ServiceLayerDataAccessException("Failed to update module", e);
        }
    }

    @Override
    public String deleteModule(Long moduleId) {
        //TODO: Secure so only course owners (instructors and institutions) can delete modules

        //Verify module exists
        CourseModule courseModule = moduleRepo.findById(moduleId).orElseThrow(
                () -> new ModuleNotFoundException(moduleId));
        //delete
        try {
            moduleRepo.delete(courseModule);
            return "Module deleted";
        } catch (Exception e) {
            throw new ServiceLayerDataAccessException("Failed to delete module", e);
        }
    }
}
