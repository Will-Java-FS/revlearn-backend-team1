package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.course.ModuleDTO;
import com.revlearn.team1.model.CourseModule;

public class ModuleMapper {

    /**
     * Maps a CourseModule entity to a ModuleDTO.
     *
     * @param courseModule the CourseModule entity
     * @return the corresponding ModuleDTO
     */
    public static ModuleDTO toDto(CourseModule courseModule) {
        if (courseModule == null) {
            return null; // Handle null case if needed
        }

        return new ModuleDTO(
                courseModule.getId(),
                courseModule.getTitle(),
                courseModule.getDescription(),
                courseModule.getOrderIndex()
        );
    }

    /**
     * Maps a ModuleDTO to a CourseModule entity.
     *
     * @param moduleDTO the ModuleDTO
     * @return the corresponding CourseModule entity
     */
    public static CourseModule toEntity(ModuleDTO moduleDTO) {
        if (moduleDTO == null) {
            return null; // Handle null case if needed
        }

        CourseModule courseModule = new CourseModule();
        courseModule.setId(moduleDTO.id());
        courseModule.setTitle(moduleDTO.title());
        courseModule.setDescription(moduleDTO.description());
        courseModule.setOrderIndex(moduleDTO.orderIndex());

        return courseModule;
    }


    /**
     * Updates an existing CourseModule entity with information from the ModuleDTO.
     *
     * @param courseModule the original CourseModule entity to be updated
     * @param moduleDTO    the ModuleDTO containing updated information
     */
    public static void updateEntityFromDto(CourseModule courseModule, ModuleDTO moduleDTO) {
        if (courseModule == null || moduleDTO == null) {
            //TODO: Test this exception.  Add to GlobalExceptionHandler if needed.
            throw new IllegalArgumentException("CourseModule and ModuleDTO must not be null");
        }

        courseModule.setTitle(moduleDTO.title());
        courseModule.setDescription(moduleDTO.description());
        courseModule.setOrderIndex(moduleDTO.orderIndex());
    }
}
