package com.revlearn.team1.service.module;

import com.revlearn.team1.dto.course.ModuleDTO;
import com.revlearn.team1.exceptions.ModuleNotFoundException;
import com.revlearn.team1.exceptions.ServiceLayerDataAccessException;
import com.revlearn.team1.exceptions.course.CourseNotFoundException;
import com.revlearn.team1.mapper.ModuleMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.CourseModule;
import com.revlearn.team1.repository.CourseRepo;
import com.revlearn.team1.repository.ModuleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleServiceImp implements ModuleService {
    private final CourseRepo courseRepo;
    private final ModuleRepo moduleRepo;

    @Override
    public List<ModuleDTO> getModulesByCourseId(Long courseId) {
        //TODO: Secure so only course affiliated users can access (enrolled students, assigned educators, & institution)
        //TODO: Consider relocation to CourseService class
        Course course = courseRepo.findById(courseId).orElseThrow(
                () -> new CourseNotFoundException("Course not found", courseId));
        return course.getCourseModules().stream().map(ModuleMapper::toDto).toList();
    }

    @Override
    public ModuleDTO getModuleById(Long moduleId) {
        //TODO: Secure so only course affiliated users can access (enrolled students, assigned educators, & institution)
        return moduleRepo.findById(moduleId).map(ModuleMapper::toDto).orElseThrow(
                () -> new ModuleNotFoundException(moduleId));
    }

    @Override
    public ModuleDTO createModule(ModuleDTO moduleDTO) {
        //TODO: implement static set in course to ensure uniqueness of CourseModule orderIndexes
//         = (long) course.getCourseModules().size();
        try {
            return ModuleMapper.toDto(moduleRepo.save(ModuleMapper.toEntity(moduleDTO)));
        } catch (Exception e) {
            throw new ServiceLayerDataAccessException("Failed to create module", e);
        }
    }

    @Override
    public ModuleDTO updateModule(Long moduleId, ModuleDTO moduleDTO) {
        //TODO: Secure so only course owners (instructors and institutions) can update modules

        //Verify module exists
        CourseModule courseModule = moduleRepo.findById(moduleId).orElseThrow(
                () -> new ModuleNotFoundException(moduleId));
        //Update module
        ModuleMapper.updateEntityFromDto(courseModule, moduleDTO);
        try {
            return ModuleMapper.toDto(moduleRepo.save(courseModule));
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
        try {
            moduleRepo.delete(courseModule);
            return "Module deleted";
        } catch (Exception e) {
            throw new ServiceLayerDataAccessException("Failed to delete module", e);
        }
    }
}
