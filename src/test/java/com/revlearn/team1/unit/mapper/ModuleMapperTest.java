package com.revlearn.team1.unit.mapper;

import com.revlearn.team1.dto.module.ModuleResDTO;
import com.revlearn.team1.dto.module.ModuleReqDTO;
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
import static org.mockito.BDDMockito.given;

class ModuleMapperTest {

    @Mock
    private CourseRepo courseRepo;

    @InjectMocks
    private ModuleMapper moduleMapper;

    private CourseModule courseModule;
    private ModuleResDTO moduleResDTO;
    private ModuleReqDTO moduleReqDTO;

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

        moduleResDTO = new ModuleResDTO(1L, "Module 1", "Description", 1L, 1L);
        moduleReqDTO = new ModuleReqDTO("Module 1", "Description");
    }

    @Test
    void toDto_ShouldMapCourseModuleToModuleDTO() {
        // Act
        ModuleResDTO result = moduleMapper.toResDto(courseModule);

        // Assert
        assertNotNull(result);
        assertEquals(moduleResDTO, result);
    }

    @Test
    void toDto_ShouldReturnNull_WhenCourseModuleIsNull() {
        // Act
        ModuleResDTO result = moduleMapper.toResDto(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_ShouldMapModuleDTOToCourseModule_WhenCourseExists() {
        // Arrange
        given(courseRepo.findById(1L)).willReturn(Optional.of(course));

        // Act
        CourseModule result = moduleMapper.toEntityFromReqDto(moduleReqDTO);

        // Assert
        assertNotNull(result);
        assertEquals(courseModule.getTitle(), result.getTitle());
        assertEquals(courseModule.getDescription(), result.getDescription());
    }

    @Test
    void toEntity_ShouldReturnNull_WhenModuleDTOIsNull() {
        // Act
        CourseModule result = moduleMapper.toEntityFromReqDto(null);

        // Assert
        assertNull(result);
    }

    @Test
    void updateEntityFromDto_ShouldUpdateCourseModule_WhenValidInputs() {
        // Act
        moduleMapper.updateEntityFromReqDto(courseModule, moduleReqDTO);

        // Assert
        assertEquals(moduleResDTO.title(), courseModule.getTitle());
        assertEquals(moduleResDTO.description(), courseModule.getDescription());
        assertEquals(moduleResDTO.orderIndex(), courseModule.getOrderIndex());
    }

    @Test
    void updateEntityFromDto_ShouldThrowIllegalArgumentException_WhenCourseModuleIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> moduleMapper.updateEntityFromReqDto(null, moduleReqDTO));
    }

    @Test
    void updateEntityFromDto_ShouldThrowIllegalArgumentException_WhenModuleDTOIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> moduleMapper.updateEntityFromReqDto(courseModule, null));
    }
}
