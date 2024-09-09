package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.module.ModuleResDTO;
import com.revlearn.team1.dto.module.ModuleReqDTO;
import com.revlearn.team1.model.Module;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ModuleMapper {

    public ModuleResDTO toResDto(Module module) {
        if (module == null) {
            return null; // Handle null case if needed
        }

        return new ModuleResDTO(
                module.getId(),
                module.getTitle(),
                module.getDescription(),
                module.getOrderIndex(),
                module.getCourse().getId()
        );
    }

    public Module toEntityFromReqDto(ModuleReqDTO moduleReqDTO) {
        if (moduleReqDTO == null) {
            return null; // Handle null case if needed
        }

        Module module = new Module();
        module.setTitle(moduleReqDTO.title());
        module.setDescription(moduleReqDTO.description());

        return module;
    }

    public void updateEntityFromReqDto(Module module, ModuleReqDTO moduleReqDTO) {
        if (module == null || moduleReqDTO == null) {
            //TODO: Test this exception.  Add to GlobalExceptionHandler if needed.
            throw new IllegalArgumentException("CourseModule and ModuleDTO must not be null");
        }

        module.setTitle(moduleReqDTO.title());
        module.setDescription(moduleReqDTO.description());
    }
}
