package com.revlearn.team1.unit.service;

import com.revlearn.team1.dto.module.ModuleResDTO;
import com.revlearn.team1.dto.module.ModuleReqDTO;
import com.revlearn.team1.exceptions.ModuleNotFoundException;
import com.revlearn.team1.exceptions.ServiceLayerDataAccessException;
import com.revlearn.team1.exceptions.course.CourseNotFoundException;
import com.revlearn.team1.mapper.ModuleMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.Module;
import com.revlearn.team1.repository.CourseRepo;
import com.revlearn.team1.repository.ModuleRepo;
import com.revlearn.team1.service.accessControl.AccessControlService;
import com.revlearn.team1.service.module.ModuleService;
import com.revlearn.team1.service.module.ModuleServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

class ModuleServiceTest {

    @Mock
    private ModuleRepo moduleRepo;

    @Mock
    private ModuleMapper moduleMapper;
    @Mock
    private CourseRepo courseRepo;
    @Mock
    private AccessControlService accessControlService;
    @InjectMocks
    private ModuleServiceImp moduleServiceImp;

    private ModuleService moduleService;

    private ModuleResDTO moduleResDTO;
    private ModuleReqDTO moduleReqDTO;

    private Module module;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize a sample ModuleDTO and CourseModule for the tests
        moduleResDTO = new ModuleResDTO(1L, "Module 1", "Description", 1L, 1L);

        moduleReqDTO = new ModuleReqDTO("Module 1", "Description");
        module = new Module();
        module.setId(1L);
        module.setTitle("Module 1");
        module.setDescription("Description");
        module.setOrderIndex(1L);

        Course course = new Course();
        course.setModules(List.of(module));
        module.setCourse(course);

        // Assign the implementation to the interface reference
        moduleService = moduleServiceImp;
    }

    @Test
    void getModuleById_ShouldReturnModuleDTO_WhenModuleExists() {
        // Arrange
        given(moduleRepo.findById(1L)).willReturn(Optional.of(module));
        given(moduleMapper.toResDto(module)).willReturn(moduleResDTO);

        // Act
        ModuleResDTO result = moduleService.getModuleById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(moduleResDTO, result);
    }

    @Test
    void getModuleById_ShouldThrowModuleNotFoundException_WhenModuleDoesNotExist() {
        // Arrange
        given(moduleRepo.findById(1L)).willReturn(Optional.empty());

        // Act & Assert
        ModuleNotFoundException thrownException = assertThrows(
                ModuleNotFoundException.class,
                () -> moduleService.getModuleById(1L),
                "Expected getModuleById() to throw ModuleNotFoundException, but it didn't");

        assertEquals("Module by id 1 not found.", thrownException.getMessage());
    }

    @Test
    void createModule_ShouldReturnModuleDTO_WhenSaveIsSuccessful() {
        // Arrange
        long courseId = 1;
        Course course = new Course();

        given(moduleMapper.toEntityFromReqDto(moduleReqDTO)).willReturn(module);
        given(moduleRepo.save(any(Module.class))).willReturn(module);
        given(courseRepo.findById(1L)).willReturn(Optional.of(course));
        given(moduleMapper.toResDto(module)).willReturn(moduleResDTO);


        // Act
        ModuleResDTO result = moduleService.createModule(courseId, moduleReqDTO);

        // Assert
        assertNotNull(result);
        assertEquals(moduleResDTO, result);
    }

    @Test
    void createModule_ShouldThrowCourseNotFoundException_WhenCourseDoesNotExist() {
        // Arrange
        long courseId = 1;
        given(moduleMapper.toEntityFromReqDto(moduleReqDTO)).willReturn(module);
        given(courseRepo.findById(anyLong())).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(CourseNotFoundException.class, () -> moduleService.createModule(courseId, moduleReqDTO));
    }

    @Test
    void createModule_ShouldThrowServiceLayerDataAccessException_WhenSaveFails() {
        // Arrange
        long courseId = 1;
        Course course = new Course();
        given(moduleMapper.toEntityFromReqDto(moduleReqDTO)).willReturn(module);
        given(courseRepo.findById(1L)).willReturn(Optional.of(course));
        given(moduleRepo.save(any(Module.class))).willThrow(new RuntimeException());

        // Act & Assert
        ServiceLayerDataAccessException thrownException = assertThrows(
                ServiceLayerDataAccessException.class,
                () -> moduleService.createModule(courseId, moduleReqDTO),
                "Expected createModule() to throw ServiceLayerDataAccessException, but it didn't");

        assertEquals("Failed to create module", thrownException.getMessage());
    }

    @Test
    void updateModule_ShouldReturnUpdatedModuleDTO_WhenUpdateIsSuccessful() {
        // Arrange
        given(moduleRepo.findById(1L)).willReturn(Optional.of(module));
        doNothing().when(moduleMapper).updateEntityFromReqDto(module, moduleReqDTO);
        given(moduleRepo.save(module)).willReturn(module);
        given(moduleMapper.toResDto(module)).willReturn(moduleResDTO);

        // Act
        ModuleResDTO result = moduleService.updateModule(1L, moduleReqDTO);

        // Assert
        assertNotNull(result);
        assertEquals(moduleResDTO, result);
    }

    @Test
    void updateModule_ShouldThrowModuleNotFoundException_WhenModuleDoesNotExist() {
        // Arrange
        given(moduleRepo.findById(1L)).willReturn(Optional.empty());

        // Act & Assert
        ModuleNotFoundException thrownException = assertThrows(
                ModuleNotFoundException.class,
                () -> moduleService.updateModule(1L, moduleReqDTO),
                "Expected updateModule() to throw ModuleNotFoundException, but it didn't");

        assertEquals("Module by id 1 not found.", thrownException.getMessage());
    }

    @Test
    void updateModule_ShouldThrowServiceLayerDataAccessException_WhenUpdateFails() {
        // Arrange
        given(moduleRepo.findById(1L)).willReturn(Optional.of(module));
        doNothing().when(moduleMapper).updateEntityFromReqDto(module, moduleReqDTO);
        given(moduleRepo.save(module)).willThrow(new RuntimeException());

        // Act & Assert
        ServiceLayerDataAccessException thrownException = assertThrows(
                ServiceLayerDataAccessException.class,
                () -> moduleService.updateModule(1L, moduleReqDTO),
                "Expected updateModule() to throw ServiceLayerDataAccessException, but it didn't");

        assertEquals("Failed to update module", thrownException.getMessage());
    }

    @Test
    void deleteModule_ShouldReturnSuccessMessage_WhenDeleteIsSuccessful() {
        // Arrange
        given(moduleRepo.findById(1L)).willReturn(Optional.of(module));
        doNothing().when(moduleRepo).delete(module);

        // Act
        String result = moduleService.deleteModule(1L);

        // Assert
        assertEquals("Module deleted", result);
    }

    @Test
    void deleteModule_ShouldThrowModuleNotFoundException_WhenModuleDoesNotExist() {
        // Arrange
        given(moduleRepo.findById(1L)).willReturn(Optional.empty());

        // Act & Assert
        ModuleNotFoundException thrownException = assertThrows(
                ModuleNotFoundException.class,
                () -> moduleService.deleteModule(1L),
                "Expected deleteModule() to throw ModuleNotFoundException, but it didn't");

        assertEquals("Module by id 1 not found.", thrownException.getMessage());
    }

    @Test
    void deleteModule_ShouldThrowServiceLayerDataAccessException_WhenDeleteFails() {
        // Arrange
        given(moduleRepo.findById(1L)).willReturn(Optional.of(module));
        doThrow(new RuntimeException()).when(moduleRepo).delete(module);

        // Act & Assert
        ServiceLayerDataAccessException thrownException = assertThrows(
                ServiceLayerDataAccessException.class,
                () -> moduleService.deleteModule(1L),
                "Expected deleteModule() to throw ServiceLayerDataAccessException, but it didn't");

        assertEquals("Failed to delete module", thrownException.getMessage());
    }
}
