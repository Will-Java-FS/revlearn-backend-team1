package com.revlearn.team1.service.program;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.course.response.CourseResDTO;
import com.revlearn.team1.dto.program.ProgramReqDTO;
import com.revlearn.team1.dto.program.ProgramResDTO;
import com.revlearn.team1.dto.user.UserResDTO;
import com.revlearn.team1.enums.Roles;
import com.revlearn.team1.exceptions.ProgramNotFoundException;
import com.revlearn.team1.exceptions.UserNotAuthorizedException;
import com.revlearn.team1.exceptions.course.CourseNotFoundException;
import com.revlearn.team1.mapper.CourseMapper;
import com.revlearn.team1.mapper.ProgramMapper;
import com.revlearn.team1.mapper.UserMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.Program;
import com.revlearn.team1.repository.CourseRepo;
import com.revlearn.team1.repository.ProgramRepo;
import com.revlearn.team1.service.securityContext.SecurityContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProgramServiceImp implements ProgramService {
    private final ProgramRepo programRepo;
    private final ProgramMapper programMapper;
    private final CourseMapper courseMapper;
    private final UserMapper userMapper;
    private final CourseRepo courseRepo;

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

        programRepo.delete(program);

        return new MessageDTO("Successfully deleted program with id: " + programId);
    }

    @Override
    public MessageDTO addCourseToProgram(Long programId, Long courseId) {

        //verify request authorization
        if (SecurityContextService.getUserRole() != Roles.INSTITUTION) {
            throw new UserNotAuthorizedException("Unauthorized to add course to program.  Must be institution (admin) account.");
        }

        //verify program exists
        Program program = programRepo.findById(programId)
                .orElseThrow(() -> new ProgramNotFoundException(
                        String.format("Program with id: %d not found", programId)));

        //verify course is not already in program
        if (program.getCourses().stream().anyMatch(c -> Objects.equals(c.getId(), courseId))) {
            return new MessageDTO("Course with id: " + courseId + " is already in program with id: " + programId);
        }

        //verify course exists
        Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(
                        "addCourseToProgram()", courseId));

        //add course to program
        program.getCourses().add(course);
        course.getPrograms().add(program);

        //save program
        programRepo.save(program);
        courseRepo.save(course);

        return new MessageDTO("Successfully added course with id: " + courseId + " to program with id: " + programId);
    }

    @Override
    public MessageDTO removeCourseFromProgram(Long programId, Long courseId) {

        //verify request authorization
        if (SecurityContextService.getUserRole() != Roles.INSTITUTION) {
            throw new UserNotAuthorizedException("Unauthorized to remove course from program.  Must be institution (admin) account.");
        }

        //verify program exists
        Program program = programRepo.findById(programId)
                .orElseThrow(() -> new ProgramNotFoundException(
                        String.format("Program with id: %d not found", programId)));

        //verify course exists in program
        Optional<Course> course = program.getCourses().stream().filter(c -> Objects.equals(c.getId(), courseId)).findFirst();
        if (course.isEmpty()) {
            return new MessageDTO("Course with id: " + courseId + " is not in program with id: " + programId);
        }
        Course foundCourse = course.get();

        //remove course from program
        program.getCourses().remove(foundCourse);
        foundCourse.getPrograms().remove(program);

        //save program
        programRepo.save(program);
        courseRepo.save(foundCourse);

        return new MessageDTO("Successfully removed course with id: " + courseId + " from program with id: " + programId);
    }
}
