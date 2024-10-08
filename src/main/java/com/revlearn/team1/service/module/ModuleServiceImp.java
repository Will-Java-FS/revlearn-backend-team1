package com.revlearn.team1.service.module;

import com.revlearn.team1.dto.exam.ExamResDTO;
import com.revlearn.team1.dto.module.ModuleReqDTO;
import com.revlearn.team1.dto.module.ModuleResDTO;
import com.revlearn.team1.dto.page.PageResDTO;
import com.revlearn.team1.exceptions.ModuleNotFoundException;
import com.revlearn.team1.exceptions.ServiceLayerDataAccessException;
import com.revlearn.team1.exceptions.course.CourseNotFoundException;
import com.revlearn.team1.mapper.ExamMapper;
import com.revlearn.team1.mapper.ModuleMapper;
import com.revlearn.team1.mapper.PageMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.Module;
import com.revlearn.team1.repository.CourseRepo;
import com.revlearn.team1.repository.ModuleRepo;
import com.revlearn.team1.service.accessControl.AccessControlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleServiceImp implements ModuleService {
    public final CourseRepo courseRepo;
    private final ModuleRepo moduleRepo;
    private final ModuleMapper moduleMapper;
    private final AccessControlService accessControlService;
    private final PageMapper pageMapper;
    private final ExamMapper examMapper;

    @Override
    public ModuleResDTO getModuleById(Long moduleId) {
        //Verify module exists
        Module module = moduleRepo.findById(moduleId).orElseThrow(
                () -> new ModuleNotFoundException(moduleId));

        //Verify only course affiliated users can access (enrolled students, assigned educators, & institution)
        Course course = module.getCourse();
        accessControlService.verifyStudentLevelAccess(course);

        return moduleMapper.toResDto(module);
    }

    @Override
    public ModuleResDTO createModule(Long courseId, ModuleReqDTO moduleReqDTO) {
        //Verify course exists
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("ModuleServiceImp#createModule()", courseId));

        //only course owners can create (assigned educators, & institution)
        accessControlService.verifyEducatorLevelAccess(course);

        //Create module
        Module module = moduleMapper.toEntityFromReqDto(moduleReqDTO);

        //Set Course
        module.setCourse(course);

        //Set orderIndex: default to last in list
        module.setOrderIndex((long) module.getCourse().getModules().size());

        try {
            return moduleMapper.toResDto(moduleRepo.save(module));
        } catch (Exception e) {
            throw new ServiceLayerDataAccessException("Failed to create module", e);
        }
    }

    @Override
    public ModuleResDTO updateModule(Long moduleId, ModuleReqDTO moduleReqDTO) {

        //Verify module exists
        Module module = moduleRepo.findById(moduleId).orElseThrow(
                () -> new ModuleNotFoundException(moduleId));

        Course course = module.getCourse();
        //only course owners can update (assigned educators, & institution)
        accessControlService.verifyEducatorLevelAccess(course);

        //Update module
        moduleMapper.updateEntityFromReqDto(module, moduleReqDTO);

        try {
            return moduleMapper.toResDto(moduleRepo.save(module));
        } catch (Exception e) {
            throw new ServiceLayerDataAccessException("Failed to update module", e);
        }
    }

    @Override
    public String deleteModule(Long moduleId) {

        //Verify module exists
        Module module = moduleRepo.findById(moduleId).orElseThrow(
                () -> new ModuleNotFoundException(moduleId));

        Course course = module.getCourse();
        //only course owners can delete (assigned educators, & institution)
        accessControlService.verifyEducatorLevelAccess(course);

        //delete
        try {
            moduleRepo.delete(module);
            return "Module deleted";
        } catch (Exception e) {
            throw new ServiceLayerDataAccessException("Failed to delete module", e);
        }
    }

    @Override
    public List<PageResDTO> getModulePages(Long moduleId) {
        //Verify module exists
        Module module = moduleRepo.findById(moduleId).orElseThrow(
                () -> new ModuleNotFoundException(moduleId));

        //verify only course affiliated users can access (enrolled students, assigned educators, & institution)
        Course course = module.getCourse();
        accessControlService.verifyStudentLevelAccess(course);

        //TODO: convert to DTOs
        return module.getPages().stream().map(pageMapper::toPageResDTO).toList();
    }

    @Override
    public List<ExamResDTO> getExams(Long moduleId) {

        //Verify module exists
        Module module = moduleRepo.findById(moduleId).orElseThrow(
                () -> new ModuleNotFoundException(moduleId));

        //verify only course affiliated users can access (enrolled students, assigned educators, & institution)
        Course course = module.getCourse();
        accessControlService.verifyStudentLevelAccess(course);

        return module.getExams().stream().map(examMapper::toExamResDTO).toList();
    }

    //TODO: implement method to allow client to rearrange modules' orderIndexes
    // static set in course to ensure uniqueness of CourseModule orderIndexes ?
    // Get all modules in course, sort by orderIndex, and update each with new orderIndex
}
