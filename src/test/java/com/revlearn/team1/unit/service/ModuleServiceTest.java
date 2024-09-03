package com.revlearn.team1.unit.service;

import com.revlearn.team1.dto.course.ModuleDTO;
import com.revlearn.team1.exceptions.ModuleNotFoundException;
import com.revlearn.team1.exceptions.ServiceLayerDataAccessException;
import com.revlearn.team1.mapper.ModuleMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.CourseModule;
import com.revlearn.team1.repository.ModuleRepo;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

class ModuleServiceTest {

    @Mock
    private ModuleRepo moduleRepo;

    @Mock
    private ModuleMapper moduleMapper;

    @InjectMocks
    private ModuleServiceImp moduleServiceImp;

    private ModuleService moduleService;

    private ModuleDTO moduleDTO;
    private CourseModule courseModule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize a sample ModuleDTO and CourseModule for the tests
        moduleDTO = new ModuleDTO(1L, "Module 1", "Description", 1L, 1L);
        courseModule = new CourseModule();
        courseModule.setId(1L);
        courseModule.setTitle("Module 1");
        courseModule.setDescription("Description");
        courseModule.setOrderIndex(1L);

        Course course = new Course();
        course.setCourseModules(List.of(courseModule));
        courseModule.setCourse(course);

        // Assign the implementation to the interface reference
        moduleService = moduleServiceImp;
    }

    @Test
    void getModuleById_ShouldReturnModuleDTO_WhenModuleExists() {
        // Arrange
        given(moduleRepo.findById(1L)).willReturn(Optional.of(courseModule));
        given(moduleMapper.toDto(courseModule)).willReturn(moduleDTO);

        // Act
        ModuleDTO result = moduleService.getModuleById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(moduleDTO, result);
    }

    @Test
    void getModuleById_ShouldThrowModuleNotFoundException_WhenModuleDoesNotExist() {
        // Arrange
        given(moduleRepo.findById(1L)).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(ModuleNotFoundException.class, () -> moduleService.getModuleById(1L));
    }

    @Test
    void createModule_ShouldReturnModuleDTO_WhenSaveIsSuccessful() {
        // Arrange
        given(moduleMapper.toEntity(moduleDTO)).willReturn(courseModule);
        given(moduleRepo.save(any(CourseModule.class))).willReturn(courseModule);
        given(moduleMapper.toDto(courseModule)).willReturn(moduleDTO);


        // Act
        ModuleDTO result = moduleService.createModule(moduleDTO);

        // Assert
        assertNotNull(result);
        assertEquals(moduleDTO, result);
    }

    @Test
    void createModule_ShouldThrowServiceLayerDataAccessException_WhenSaveFails() {
        // Arrange
        given(moduleMapper.toEntity(moduleDTO)).willReturn(courseModule);
        given(moduleRepo.save(any(CourseModule.class))).willThrow(new RuntimeException());

        // Act & Assert
        assertThrows(ServiceLayerDataAccessException.class, () -> moduleService.createModule(moduleDTO));
    }

    @Test
    void updateModule_ShouldReturnUpdatedModuleDTO_WhenUpdateIsSuccessful() {
        // Arrange
        given(moduleRepo.findById(1L)).willReturn(Optional.of(courseModule));
        doNothing().when(moduleMapper).updateEntityFromDto(courseModule, moduleDTO);
        given(moduleRepo.save(courseModule)).willReturn(courseModule);
        given(moduleMapper.toDto(courseModule)).willReturn(moduleDTO);

        // Act
        ModuleDTO result = moduleService.updateModule(1L, moduleDTO);

        // Assert
        assertNotNull(result);
        assertEquals(moduleDTO, result);
    }

    @Test
    void updateModule_ShouldThrowModuleNotFoundException_WhenModuleDoesNotExist() {
        // Arrange
        given(moduleRepo.findById(1L)).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(ModuleNotFoundException.class, () -> moduleService.updateModule(1L, moduleDTO));
    }

    @Test
    void updateModule_ShouldThrowServiceLayerDataAccessException_WhenUpdateFails() {
        // Arrange
        given(moduleRepo.findById(1L)).willReturn(Optional.of(courseModule));
        doNothing().when(moduleMapper).updateEntityFromDto(courseModule, moduleDTO);
        given(moduleRepo.save(courseModule)).willThrow(new RuntimeException());

        // Act & Assert
        assertThrows(ServiceLayerDataAccessException.class, () -> moduleService.updateModule(1L, moduleDTO));
    }

    @Test
    void deleteModule_ShouldReturnSuccessMessage_WhenDeleteIsSuccessful() {
        // Arrange
        given(moduleRepo.findById(1L)).willReturn(Optional.of(courseModule));
        doNothing().when(moduleRepo).delete(courseModule);

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
        assertThrows(ModuleNotFoundException.class, () -> moduleService.deleteModule(1L));
    }

    @Test
    void deleteModule_ShouldThrowServiceLayerDataAccessException_WhenDeleteFails() {
        // Arrange
        given(moduleRepo.findById(1L)).willReturn(Optional.of(courseModule));
        doThrow(new RuntimeException()).when(moduleRepo).delete(courseModule);

        // Act & Assert
        assertThrows(ServiceLayerDataAccessException.class, () -> moduleService.deleteModule(1L));
    }
}
