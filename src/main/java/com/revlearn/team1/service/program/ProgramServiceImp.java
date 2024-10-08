package com.revlearn.team1.service.program;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.course.response.CourseResDTO;
import com.revlearn.team1.dto.program.ProgramReqDTO;
import com.revlearn.team1.dto.program.ProgramResDTO;
import com.revlearn.team1.dto.user.UserResDTO;
import com.revlearn.team1.enums.Roles;
import com.revlearn.team1.exceptions.ProgramNotFoundException;
import com.revlearn.team1.exceptions.UserNotAuthorizedException;
import com.revlearn.team1.mapper.ProgramMapper;
import com.revlearn.team1.mapper.UserMapper;
import com.revlearn.team1.model.Program;
import com.revlearn.team1.repository.ProgramRepo;
import com.revlearn.team1.service.programCourse.ProgramCourseService;
import com.revlearn.team1.service.securityContext.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


//TODO: Abstract security checks to a service

@Service
@RequiredArgsConstructor
public class ProgramServiceImp implements ProgramService {
    private final ProgramRepo programRepo;
    private final ProgramMapper programMapper;
    private final ProgramCourseService programCourseService;
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

        return programCourseService.getProgramCourses(program);
    }

    @Override
    public List<UserResDTO> getProgramStudents(Long programId) {
        //verify request authorization
        if (SecurityContextService.getUserRole() != Roles.INSTITUTION) {
            throw new UserNotAuthorizedException("Unauthorized to get program students.  Must be institution (admin) account.");
        }

        Program program = programRepo.findById(programId)
                .orElseThrow(() -> new ProgramNotFoundException(
                        String.format("Program with id: %d not found", programId)));

        return program.getStudents().stream().map(userMapper::toResDTO).toList();
    }

    @Override
    public ProgramResDTO createProgram(ProgramReqDTO programReqDTO) {
        //verify request authorization
        if (SecurityContextService.getUserRole() != Roles.INSTITUTION) {
            throw new UserNotAuthorizedException("Unauthorized to create program.  Must be institution (admin) account.");
        }

        Program program = programMapper.toEntity(programReqDTO);
        return programMapper.toResponseDTO(programRepo.save(program));
    }

    @Override
    public ProgramResDTO updateProgram(Long programId, ProgramReqDTO programReqDTO) {
        //verify request authorization
        if (SecurityContextService.getUserRole() != Roles.INSTITUTION) {
            throw new UserNotAuthorizedException("Unauthorized to update program.  Must be institution (admin) account.");
        }

        Program program = programRepo.findById(programId)
                .orElseThrow(() -> new ProgramNotFoundException(
                        String.format("Program with id: %d not found", programId)));

        programMapper.updateEntityFromDTO(programReqDTO, program);

        return programMapper.toResponseDTO(programRepo.save(program));
    }

    @Override
    public MessageDTO deleteProgram(Long programId) {
        //verify request authorization
        if (SecurityContextService.getUserRole() != Roles.INSTITUTION) {
            throw new UserNotAuthorizedException("Unauthorized to delete program.  Must be institution (admin) account.");
        }

        Program program = programRepo.findById(programId)
                .orElseThrow(() -> new ProgramNotFoundException(
                        String.format("Program with id: %d not found", programId)));

        //Remove all courses from program
        programCourseService.removeAllCoursesFromProgram(program);


        programRepo.delete(program);

        return new MessageDTO("Successfully deleted program with id: " + programId);
    }

    @Override
    public MessageDTO addCourseToProgram(Long programId, Long courseId) {
        return programCourseService.addCourseToProgram(programId, courseId);
    }

    @Override
    public MessageDTO removeCourseFromProgram(Long programId, Long courseId) {
        return programCourseService.removeCourseFromProgram(programId, courseId);
    }
}
