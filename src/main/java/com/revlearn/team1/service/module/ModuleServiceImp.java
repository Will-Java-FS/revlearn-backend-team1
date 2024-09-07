package com.revlearn.team1.service.module;

import com.revlearn.team1.dto.module.ModuleResDTO;
import com.revlearn.team1.dto.module.ModuleReqDTO;
import com.revlearn.team1.exceptions.ModuleNotFoundException;
import com.revlearn.team1.exceptions.ServiceLayerDataAccessException;
import com.revlearn.team1.exceptions.course.CourseNotFoundException;
import com.revlearn.team1.mapper.ModuleMapper;
import com.revlearn.team1.model.CourseModule;
import com.revlearn.team1.model.Exam;
import com.revlearn.team1.model.ModulePage;
import com.revlearn.team1.repository.CourseRepo;
import com.revlearn.team1.repository.ModuleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleServiceImp implements ModuleService {
    private final ModuleRepo moduleRepo;
    private final ModuleMapper moduleMapper;
    public final CourseRepo courseRepo;

    @Override
    public ModuleResDTO getModuleById(Long moduleId) {
        //TODO: Secure so only course affiliated users can access (enrolled students, assigned educators, & institution)
        return moduleRepo.findById(moduleId).map(moduleMapper::toResDto).orElseThrow(
                () -> new ModuleNotFoundException(moduleId));
    }

    @Override
    public ModuleResDTO createModule(Long courseId, ModuleReqDTO moduleReqDTO) {
        CourseModule courseModule = moduleMapper.toEntityFromReqDto(moduleReqDTO);
        //TODO: Secure so only course owners can create (assigned educators, & institution)

        //Set Course
        courseModule.setCourse(courseRepo.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("ModuleServiceImp#createModule()", courseId)));

        //Set orderIndex: default to last in list
        courseModule.setOrderIndex((long) courseModule.getCourse().getCourseModules().size());

        try {
            return moduleMapper.toResDto(moduleRepo.save(courseModule));
        } catch (Exception e) {
            throw new ServiceLayerDataAccessException("Failed to create module", e);
        }
    }

    @Override
    public ModuleResDTO updateModule(Long moduleId, ModuleReqDTO moduleReqDTO) {
        //TODO: Secure so only course owners (instructors and institutions) can update modules
        //TODO: implement static set in course to ensure uniqueness of CourseModule orderIndexes

        //Verify module exists
        CourseModule courseModule = moduleRepo.findById(moduleId).orElseThrow(
                () -> new ModuleNotFoundException(moduleId));

        //Update module
        moduleMapper.updateEntityFromReqDto(courseModule, moduleReqDTO);

        try {
            return moduleMapper.toResDto(moduleRepo.save(courseModule));
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

    @Override
    public List<ModulePage> getModulePages(Long moduleId) {
        //TODO: Secure so only course affiliated users can access (enrolled students, assigned educators, & institution)
        //Verify module exists
        CourseModule courseModule = moduleRepo.findById(moduleId).orElseThrow(
                () -> new ModuleNotFoundException(moduleId));
        //TODO: convert to DTOs
        return courseModule.getModulePages();
    }

    @Override
    public List<Exam> getExams(Long moduleId) {
        //TODO: Secure so only course affiliated users can access (enrolled students, assigned educators, & institution)
        //Verify module exists
        CourseModule courseModule = moduleRepo.findById(moduleId).orElseThrow(
                () -> new ModuleNotFoundException(moduleId));
        //TODO: convert to DTOs
        return courseModule.getExams();
    }
}
