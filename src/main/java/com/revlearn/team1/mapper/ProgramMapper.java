package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.program.ProgramReqDTO;
import com.revlearn.team1.dto.program.ProgramResDTO;
import com.revlearn.team1.model.Program;
import org.springframework.stereotype.Component;

@Component
public class ProgramMapper {
    // Convert Program entity to ProgramResDTO
    public ProgramResDTO toResponseDTO(Program program) {
        return new ProgramResDTO(
                program.getId(),
                program.getTitle(),
                program.getDescription(),
                program.getDepartment(),
                program.getDegree(),
                program.getDuration(),
                program.getLocation(),
                program.getFormat(),
                program.getCost()
        );
    }

    // Convert ProgramReqDTO to Program entity
    public Program toEntity(ProgramReqDTO dto) {
        Program program = new Program();
        program.setTitle(dto.title());
        program.setDescription(dto.description());
        program.setDepartment(dto.department());
        program.setDegree(dto.degree());
        program.setDuration(dto.duration());
        program.setLocation(dto.location());
        program.setFormat(dto.format());
        program.setCost(dto.cost());
        return program;
    }

    // Update existing Program entity with data from ProgramReqDTO
    public void updateEntityFromDTO(ProgramReqDTO dto, Program program) {
        if (dto.title() != null) {
            program.setTitle(dto.title());
        }
        if (dto.description() != null) {
            program.setDescription(dto.description());
        }
        if (dto.department() != null) {
            program.setDepartment(dto.department());
        }
        if (dto.degree() != null) {
            program.setDegree(dto.degree());
        }
        if (dto.duration() != null) {
            program.setDuration(dto.duration());
        }
        if (dto.location() != null) {
            program.setLocation(dto.location());
        }
        if (dto.format() != null) {
            program.setFormat(dto.format());
        }
        if(dto.cost()!=0){
            program.setCost(dto.cost());
        }
    }
}
