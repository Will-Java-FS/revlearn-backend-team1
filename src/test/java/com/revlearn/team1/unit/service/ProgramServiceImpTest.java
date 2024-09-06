package com.revlearn.team1.unit.service;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.course.response.CourseResDTO;
import com.revlearn.team1.dto.program.ProgramReqDTO;
import com.revlearn.team1.dto.program.ProgramResDTO;
import com.revlearn.team1.dto.user.UserResDTO;
import com.revlearn.team1.enums.Roles;
import com.revlearn.team1.exceptions.ProgramNotFoundException;
import com.revlearn.team1.exceptions.UserNotAuthorizedException;
import com.revlearn.team1.mapper.CourseMapper;
import com.revlearn.team1.mapper.ProgramMapper;
import com.revlearn.team1.mapper.UserMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.Program;
import com.revlearn.team1.model.User;
import com.revlearn.team1.repository.CourseRepo;
import com.revlearn.team1.repository.ProgramRepo;
import com.revlearn.team1.service.program.ProgramServiceImp;
import com.revlearn.team1.service.securityContext.SecurityContextService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ProgramServiceImpTest {

    @Mock
    private ProgramRepo programRepo;
    @Mock
    private CourseRepo courseRepo;

    @Mock
    private ProgramMapper programMapper;

    @Mock
    private CourseMapper courseMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private ProgramServiceImp programService;

    private Program program;
    private Course course;
    private ProgramReqDTO programReqDTO;
    private ProgramResDTO programResDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        program = new Program();
        program.setId(1L);
        program.setTitle("Computer Science");

        programReqDTO = new ProgramReqDTO("Computer Science", "Description", "CS Department", "Bachelor's", "4 years", "On-Campus", "Full-time", 40000);

        programResDTO = new ProgramResDTO(1L, "Computer Science", "Description", "CS Department", "Bachelor's", "4 years", "On-Campus", "Full-time", 40000);

        course = new Course();
        course.setId(123L);
    }

    @Test
    void getAllPrograms_ShouldReturnListOfPrograms() {
        when(programRepo.findAll()).thenReturn(Arrays.asList(program));
        when(programMapper.toResponseDTO(program)).thenReturn(programResDTO);

        List<ProgramResDTO> programs = programService.getAllPrograms();

        assertEquals(1, programs.size());
        assertEquals(programResDTO, programs.get(0));
        verify(programRepo).findAll();
        verify(programMapper).toResponseDTO(program);
    }

    @Test
    void getProgramById_ShouldReturnProgram() {
        when(programRepo.findById(1L)).thenReturn(Optional.of(program));
        when(programMapper.toResponseDTO(program)).thenReturn(programResDTO);

        ProgramResDTO foundProgram = programService.getProgramById(1L);

        assertEquals(programResDTO, foundProgram);
        verify(programRepo).findById(1L);
        verify(programMapper).toResponseDTO(program);
    }

    @Test
    void getProgramById_ShouldThrowException_WhenProgramNotFound() {
        when(programRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProgramNotFoundException.class, () -> programService.getProgramById(1L));
        verify(programRepo).findById(1L);
    }

    @Test
    void getProgramCourses_ShouldReturnListOfCourses() {

        program.getCourses().add(new Course());
        when(programRepo.findById(1L)).thenReturn(Optional.of(program));
        when(courseMapper.toDto(any())).thenReturn(new CourseResDTO(1L, null, null, null, "Course 1", null, null));

        List<CourseResDTO> courses = programService.getProgramCourses(1L);

        assertEquals(1, courses.size());
        assertEquals("Course 1", courses.get(0).name());
        verify(programRepo).findById(1L);
        verify(courseMapper).toDto(any());
    }

    @Test
    void getProgramCourses_ShouldThrowException_WhenProgramNotFound() {
        when(programRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProgramNotFoundException.class, () -> programService.getProgramCourses(1L));
        verify(programRepo).findById(1L);
    }

    @Test
    void getProgramStudents_ShouldReturnListOfStudents() {
        try (MockedStatic<SecurityContextService> mockedStatic = mockStatic(SecurityContextService.class)) {
            mockedStatic.when(SecurityContextService::getUserRole).thenReturn(Roles.INSTITUTION);
            program.getStudents().add(new User());
            when(programRepo.findById(1L)).thenReturn(Optional.of(program));
            when(userMapper.toResDTO(any())).thenReturn(new UserResDTO(1, "John Doe", "john.doe@example.com", Roles.STUDENT, "John", "Doe"));

            List<UserResDTO> students = programService.getProgramStudents(1L);

            assertEquals(1, students.size());
            assertEquals("John Doe", students.get(0).username());
            verify(programRepo).findById(1L);
            verify(userMapper).toResDTO(any());
        }
    }

    @Test
    void createProgram_ShouldSaveProgram() {
        try (MockedStatic<SecurityContextService> mockedStatic = mockStatic(SecurityContextService.class)) {
            mockedStatic.when(SecurityContextService::getUserRole).thenReturn(Roles.INSTITUTION);
            when(programMapper.toEntity(any(ProgramReqDTO.class))).thenReturn(program);
            when(programRepo.save(program)).thenReturn(program);
            when(programMapper.toResponseDTO(program)).thenReturn(programResDTO);

            ProgramResDTO createdProgram = programService.createProgram(programReqDTO);

            assertEquals(programResDTO, createdProgram);
            verify(programMapper).toEntity(any(ProgramReqDTO.class));
            verify(programRepo).save(program);
            verify(programMapper).toResponseDTO(program);
        }
    }

    @Test
    void updateProgram_ShouldUpdateProgram() {
        try (MockedStatic<SecurityContextService> mockedStatic = mockStatic(SecurityContextService.class)) {
            mockedStatic.when(SecurityContextService::getUserRole).thenReturn(Roles.INSTITUTION);
            when(programRepo.findById(1L)).thenReturn(Optional.of(program));
            when(programRepo.save(program)).thenReturn(program);
            when(programMapper.toResponseDTO(program)).thenReturn(programResDTO);

            ProgramResDTO updatedProgram = programService.updateProgram(1L, programReqDTO);

            assertEquals(programResDTO, updatedProgram);
            verify(programRepo).findById(1L);
            verify(programRepo).save(program);
            verify(programMapper).updateEntityFromDTO(any(ProgramReqDTO.class), eq(program));
            verify(programMapper).toResponseDTO(program);
        }
    }

    @Test
    void deleteProgram_ShouldDeleteProgram() {
        try (MockedStatic<SecurityContextService> mockedStatic = mockStatic(SecurityContextService.class)) {
            mockedStatic.when(SecurityContextService::getUserRole).thenReturn(Roles.INSTITUTION);
            when(programRepo.findById(1L)).thenReturn(Optional.of(program));

            programService.deleteProgram(1L);

            verify(programRepo).delete(program);
        }
    }

    @Test
    void deleteProgram_ShouldThrowException_WhenProgramNotFound() {
        try (MockedStatic<SecurityContextService> mockedStatic = mockStatic(SecurityContextService.class)) {
            mockedStatic.when(SecurityContextService::getUserRole).thenReturn(Roles.INSTITUTION);
            when(programRepo.findById(1L)).thenReturn(Optional.empty());

            assertThrows(ProgramNotFoundException.class, () -> programService.deleteProgram(1L));
            verify(programRepo).findById(1L);
        }
    }

    @Test
    void addCourseToProgram_ShouldThrowException_WhenUserNotAuthorized() {
        try (MockedStatic<SecurityContextService> mockedStatic = mockStatic(SecurityContextService.class)) {
            // Arrange
            mockedStatic.when(SecurityContextService::getUserRole).thenReturn(Roles.STUDENT); // Not authorized

            // Act & Assert
            assertThrows(UserNotAuthorizedException.class, () -> programService.addCourseToProgram(456L, 123L));

            // Verify that no interactions with the repository happened
            verifyNoInteractions(programRepo, courseRepo);
        }
    }

    @Test
    void addCourseToProgram_ShouldThrowException_WhenProgramNotFound() {
        try (MockedStatic<SecurityContextService> mockedStatic = mockStatic(SecurityContextService.class)) {

            // Arrange
            mockedStatic.when(SecurityContextService::getUserRole).thenReturn(Roles.INSTITUTION);
            when(programRepo.findById(anyLong())).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ProgramNotFoundException.class, () -> programService.addCourseToProgram(456L, 123L));

            // Verify the programRepo was called
            verify(programRepo).findById(456L);
            verifyNoInteractions(courseRepo);
        }
    }

    @Test
    void addCourseToProgram_ShouldReturnMessage_WhenCourseAlreadyInProgram() {
        // Arrange
        try (MockedStatic<SecurityContextService> mockedStatic = mockStatic(SecurityContextService.class)) {
            mockedStatic.when(SecurityContextService::getUserRole).thenReturn(Roles.INSTITUTION);
            when(programRepo.findById(456L)).thenReturn(Optional.of(program));
            when(courseRepo.findById(123L)).thenReturn(Optional.of(course));
            program.getCourses().add(course); // Course already in program

            // Act
            MessageDTO message = programService.addCourseToProgram(456L, 123L);

            // Assert
            assertEquals("Course with id: 123 is already in program with id: 456", message.message());
        }

    }

    @Test
    void addCourseToProgram_ShouldAddCourseToProgramAndReturnMessage() {
        // Arrange
        try (MockedStatic<SecurityContextService> mockedStatic = mockStatic(SecurityContextService.class)) {
            mockedStatic.when(SecurityContextService::getUserRole).thenReturn(Roles.INSTITUTION);
            when(programRepo.findById(456L)).thenReturn(Optional.of(program));
            when(courseRepo.findById(123L)).thenReturn(Optional.of(course));

            // Act
            MessageDTO message = programService.addCourseToProgram(456L, 123L);

            // Assert
            assertEquals("Successfully added course with id: 123 to program with id: 456", message.message());
            assertEquals(1, program.getCourses().size()); // Course added
            verify(programRepo).save(program);
            verify(courseRepo).save(course);
        }

    }

    @Test
    void removeCourseFromProgram_ShouldThrowException_WhenUserNotAuthorized() {
        try (MockedStatic<SecurityContextService> mockedStatic = mockStatic(SecurityContextService.class)) {
            // Arrange
            mockedStatic.when(SecurityContextService::getUserRole).thenReturn(Roles.STUDENT); // Not authorized

            // Act & Assert
            assertThrows(UserNotAuthorizedException.class, () -> programService.removeCourseFromProgram(456L, 123L));

            // Verify that no interactions with the repository happened
            verifyNoInteractions(programRepo, courseRepo);
        }
    }

    @Test
    void removeCourseFromProgram_ShouldThrowException_WhenProgramNotFound() {
        try (MockedStatic<SecurityContextService> mockedStatic = mockStatic(SecurityContextService.class)) {
            // Arrange
            mockedStatic.when(SecurityContextService::getUserRole).thenReturn(Roles.INSTITUTION);
            when(programRepo.findById(anyLong())).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(ProgramNotFoundException.class, () -> programService.removeCourseFromProgram(456L, 123L));

            // Verify the programRepo was called
            verify(programRepo).findById(456L);
            verifyNoInteractions(courseRepo);
        }

    }

    @Test
    void removeCourseFromProgram_ShouldReturnMessage_WhenCourseNotInProgram() {
        // Arrange
        try (MockedStatic<SecurityContextService> mockedStatic = mockStatic(SecurityContextService.class)) {
            mockedStatic.when(SecurityContextService::getUserRole).thenReturn(Roles.INSTITUTION);
            when(programRepo.findById(456L)).thenReturn(Optional.of(program));

            // Act
            MessageDTO message = programService.removeCourseFromProgram(456L, 123L);

            // Assert
            assertEquals("Course with id: 123 is not in program with id: 456", message.message());
        }
    }

    @Test
    void removeCourseFromProgram_ShouldRemoveCourseFromProgramAndReturnMessage() {
        try (MockedStatic<SecurityContextService> mockedStatic = mockStatic(SecurityContextService.class)) {
            // Arrange
            mockedStatic.when(SecurityContextService::getUserRole).thenReturn(Roles.INSTITUTION);
            when(programRepo.findById(456L)).thenReturn(Optional.of(program));
            when(courseRepo.findById(123L)).thenReturn(Optional.of(course));
            program.getCourses().add(course); // Course is in the program

            // Act
            MessageDTO message = programService.removeCourseFromProgram(456L, 123L);

            // Assert
            assertEquals("Successfully removed course with id: 123 from program with id: 456", message.message());
            assertEquals(0, program.getCourses().size()); // Course removed
            verify(programRepo).save(program);
            verify(courseRepo).save(course);
        }

    }
}
