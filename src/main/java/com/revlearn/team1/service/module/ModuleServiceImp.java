package com.revlearn.team1.service.module;

import com.revlearn.team1.dto.module.ModuleReqDTO;
import com.revlearn.team1.dto.module.ModuleResDTO;
import com.revlearn.team1.exceptions.ModuleNotFoundException;
import com.revlearn.team1.exceptions.ServiceLayerDataAccessException;
import com.revlearn.team1.exceptions.course.CourseNotFoundException;
import com.revlearn.team1.mapper.ModuleMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.CourseModule;
import com.revlearn.team1.model.Exam;
import com.revlearn.team1.model.ModulePage;
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

    @Override
    public ModuleResDTO getModuleById(Long moduleId) {
        //Verify module exists
        CourseModule courseModule = moduleRepo.findById(moduleId).orElseThrow(
                () -> new ModuleNotFoundException(moduleId));

        //Verify only course affiliated users can access (enrolled students, assigned educators, & institution)
        Course course = courseModule.getCourse();
        accessControlService.verifyStudentLevelAccess(course);

        return moduleMapper.toResDto(courseModule);
    }

    @Override
    public ModuleResDTO createModule(Long courseId, ModuleReqDTO moduleReqDTO) {
        //Verify course exists
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("ModuleServiceImp#createModule()", courseId));

        //only course owners can create (assigned educators, & institution)
        accessControlService.verifyEducatorLevelAccess(course);

        //Create module
        CourseModule courseModule = moduleMapper.toEntityFromReqDto(moduleReqDTO);

        //Set Course
        courseModule.setCourse(course);

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

        //Verify module exists
        CourseModule courseModule = moduleRepo.findById(moduleId).orElseThrow(
                () -> new ModuleNotFoundException(moduleId));

        Course course = courseModule.getCourse();
        //only course owners can update (assigned educators, & institution)
        accessControlService.verifyEducatorLevelAccess(course);

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

        //Verify module exists
        CourseModule courseModule = moduleRepo.findById(moduleId).orElseThrow(
                () -> new ModuleNotFoundException(moduleId));

        Course course = courseModule.getCourse();
        //only course owners can delete (assigned educators, & institution)
        accessControlService.verifyEducatorLevelAccess(course);

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
        //Verify module exists
        CourseModule courseModule = moduleRepo.findById(moduleId).orElseThrow(
                () -> new ModuleNotFoundException(moduleId));

        //verify only course affiliated users can access (enrolled students, assigned educators, & institution)
        Course course = courseModule.getCourse();
        accessControlService.verifyStudentLevelAccess(course);

        //TODO: convert to DTOs
        return courseModule.getModulePages();
    }

    @Override
    public List<Exam> getExams(Long moduleId) {

        //Verify module exists
        CourseModule courseModule = moduleRepo.findById(moduleId).orElseThrow(
                () -> new ModuleNotFoundException(moduleId));

        //verify only course affiliated users can access (enrolled students, assigned educators, & institution)
        Course course = courseModule.getCourse();
        accessControlService.verifyStudentLevelAccess(course);

        //TODO: convert to DTOs
        return courseModule.getExams();
    }

    //TODO: implement method to allow client to rearrange modules' orderIndexes
    // static set in course to ensure uniqueness of CourseModule orderIndexes ?
    // Get all modules in course, sort by orderIndex, and update each with new orderIndex
}
