package com.revlearn.team1.unit.mapper;

import com.revlearn.team1.dto.module.ModuleDTO;
import com.revlearn.team1.exceptions.course.CourseNotFoundException;
import com.revlearn.team1.mapper.ModuleMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.CourseModule;
import com.revlearn.team1.repository.CourseRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

class ModuleMapperTest {

    @Mock
    private CourseRepo courseRepo;

    @InjectMocks
    private ModuleMapper moduleMapper;

    private CourseModule courseModule;
    private ModuleDTO moduleDTO;
    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test data
        course = new Course();
        course.setId(1L);

        courseModule = new CourseModule();
        courseModule.setId(1L);
        courseModule.setTitle("Module 1");
        courseModule.setDescription("Description");
        courseModule.setOrderIndex(1L);
        courseModule.setCourse(course);

        moduleDTO = new ModuleDTO(1L, "Module 1", "Description", 1L, 1L);
    }

    @Test
    void toDto_ShouldMapCourseModuleToModuleDTO() {
        // Act
        ModuleDTO result = moduleMapper.toDto(courseModule);

        // Assert
        assertNotNull(result);
        assertEquals(moduleDTO, result);
    }

    @Test
    void toDto_ShouldReturnNull_WhenCourseModuleIsNull() {
        // Act
        ModuleDTO result = moduleMapper.toDto(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_ShouldMapModuleDTOToCourseModule_WhenCourseExists() {
        // Arrange
        given(courseRepo.findById(1L)).willReturn(Optional.of(course));

        // Act
        CourseModule result = moduleMapper.toEntity(moduleDTO);

        // Assert
        assertNotNull(result);
        assertEquals(courseModule.getId(), result.getId());
        assertEquals(courseModule.getTitle(), result.getTitle());
        assertEquals(courseModule.getDescription(), result.getDescription());
        assertEquals(courseModule.getOrderIndex(), result.getOrderIndex());
        assertEquals(courseModule.getCourse().getId(), result.getCourse().getId());
    }

    @Test
    void toEntity_ShouldThrowCourseNotFoundException_WhenCourseDoesNotExist() {
        // Arrange
        given(courseRepo.findById(anyLong())).willReturn(Optional.empty());

        // Act & Assert
        assertThrows(CourseNotFoundException.class, () -> moduleMapper.toEntity(moduleDTO));
    }

    @Test
    void toEntity_ShouldReturnNull_WhenModuleDTOIsNull() {
        // Act
        CourseModule result = moduleMapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void updateEntityFromDto_ShouldUpdateCourseModule_WhenValidInputs() {
        // Act
        moduleMapper.updateEntityFromDto(courseModule, moduleDTO);

        // Assert
        assertEquals(moduleDTO.title(), courseModule.getTitle());
        assertEquals(moduleDTO.description(), courseModule.getDescription());
        assertEquals(moduleDTO.orderIndex(), courseModule.getOrderIndex());
    }

    @Test
    void updateEntityFromDto_ShouldThrowIllegalArgumentException_WhenCourseModuleIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> moduleMapper.updateEntityFromDto(null, moduleDTO));
    }

    @Test
    void updateEntityFromDto_ShouldThrowIllegalArgumentException_WhenModuleDTOIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> moduleMapper.updateEntityFromDto(courseModule, null));
    }
}
