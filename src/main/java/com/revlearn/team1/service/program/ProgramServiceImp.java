package com.revlearn.team1.service.program;

import com.revlearn.team1.dto.course.response.CourseResDTO;
import com.revlearn.team1.dto.program.ProgramReqDTO;
import com.revlearn.team1.dto.program.ProgramResDTO;
import com.revlearn.team1.dto.user.UserDTO;
import com.revlearn.team1.dto.user.UserResDTO;
import com.revlearn.team1.exceptions.ProgramNotFoundException;
import com.revlearn.team1.mapper.CourseMapper;
import com.revlearn.team1.mapper.ProgramMapper;
import com.revlearn.team1.mapper.UserMapper;
import com.revlearn.team1.model.Program;
import com.revlearn.team1.repository.ProgramRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramServiceImp implements ProgramService {
    private final ProgramRepo programRepo;
    private final ProgramMapper programMapper;
    private final CourseMapper courseMapper;
    private final UserMapper userMapper;

    @Override
    public List<ProgramResDTO> getAllPrograms() {
        return programRepo.findAll().stream().map(programMapper::toResponseDTO).toList();
    }

    @Override
    public ProgramResDTO getProgramById(Long programId) {
        return programRepo.findById(programId).map(programMapper::toResponseDTO)
                .orElseThrow(() -> new ProgramNotFoundException(
                        String.format("Program with id: %d not found", programId)));
    }

    @Override
    public List<CourseResDTO> getProgramCourses(Long programId) {
        Program program = programRepo.findById(programId)
                .orElseThrow(() -> new ProgramNotFoundException(
                        String.format("Program with id: %d not found", programId)));

        return program.getCourses().stream().map(courseMapper::toDto).toList();
    }

    @Override
    public List<UserResDTO> getProgramStudents(Long programId) {
        Program program = programRepo.findById(programId)
                .orElseThrow(() -> new ProgramNotFoundException(
                        String.format("Program with id: %d not found", programId)));

        return program.getStudents().stream().map(userMapper::toResDTO).toList();
    }

    @Override
    public ProgramResDTO createProgram(ProgramReqDTO programReqDTO) {
        Program program = programMapper.toEntity(programReqDTO);
        return programMapper.toResponseDTO(programRepo.save(program));
    }

    @Override
    public ProgramResDTO updateProgram(Long programId, ProgramReqDTO programReqDTO) {
        Program program = programRepo.findById(programId)
                .orElseThrow(() -> new ProgramNotFoundException(
                        String.format("Program with id: %d not found", programId)));

        programMapper.updateEntityFromDTO(programReqDTO, program);

        return programMapper.toResponseDTO(programRepo.save(program));
    }

    @Override
    public void deleteProgram(Long programId) {
        Program program = programRepo.findById(programId)
                .orElseThrow(() -> new ProgramNotFoundException(
                        String.format("Program with id: %d not found", programId)));
        programRepo.delete(program);
    }
}
