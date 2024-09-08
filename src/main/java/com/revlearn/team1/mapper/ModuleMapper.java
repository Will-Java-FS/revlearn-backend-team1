package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.module.ModuleResDTO;
import com.revlearn.team1.dto.module.ModuleReqDTO;
import com.revlearn.team1.model.CourseModule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModuleMapper {

    public ModuleResDTO toResDto(CourseModule courseModule) {
        if (courseModule == null) {
            return null; // Handle null case if needed
        }

        return new ModuleResDTO(
                courseModule.getId(),
                courseModule.getTitle(),
                courseModule.getDescription(),
                courseModule.getOrderIndex(),
                courseModule.getCourse().getId()
        );
    }

    public CourseModule toEntityFromReqDto(ModuleReqDTO moduleReqDTO) {
        if (moduleReqDTO == null) {
            return null; // Handle null case if needed
        }

        CourseModule courseModule = new CourseModule();
        courseModule.setTitle(moduleReqDTO.title());
        courseModule.setDescription(moduleReqDTO.description());

        return courseModule;
    }

    public void updateEntityFromReqDto(CourseModule courseModule, ModuleReqDTO moduleReqDTO) {
        if (courseModule == null || moduleReqDTO == null) {
            //TODO: Test this exception.  Add to GlobalExceptionHandler if needed.
            throw new IllegalArgumentException("CourseModule and ModuleDTO must not be null");
        }

        courseModule.setTitle(moduleReqDTO.title());
        courseModule.setDescription(moduleReqDTO.description());
    }
}
