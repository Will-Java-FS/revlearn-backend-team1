package com.revlearn.team1.unit.service;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.course.response.CourseResDTO;
import com.revlearn.team1.dto.program.ProgramReqDTO;
import com.revlearn.team1.dto.program.ProgramResDTO;
import com.revlearn.team1.exceptions.ProgramNotFoundException;
import com.revlearn.team1.exceptions.UserNotAuthorizedException;
import com.revlearn.team1.mapper.CourseMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.Program;
import com.revlearn.team1.repository.CourseRepo;
import com.revlearn.team1.repository.ProgramRepo;
import com.revlearn.team1.service.accessControl.AccessControlService;
import com.revlearn.team1.service.programCourse.ProgramCourseServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProgramCourseServiceImpTest {

    @Mock
    private ProgramRepo programRepo;

    @Mock
    private CourseRepo courseRepo;
    @Mock
    private CourseMapper courseMapper;
    @Mock
    private AccessControlService accessControlService;

    @InjectMocks
    private ProgramCourseServiceImp programCourseServiceImp;

    private Program program;
    private Course course1;
    private Course course2;
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

        course1 = new Course();
        course1.setId(123L);
    }

    @Test
    void getProgramCourses_ShouldReturnListOfCourses() {

        program.getCourses().add(new Course());
        when(courseMapper.toDto(any())).thenReturn(new CourseResDTO(1L, null, null, null, "Course 1", null, null));

        List<CourseResDTO> courses = programCourseServiceImp.getProgramCourses(program);

        assertEquals(1, courses.size());
        assertEquals("Course 1", courses.get(0).name());
        verify(courseMapper).toDto(any());
    }

    @Test
    public void testRemoveAllCoursesFromProgram_Success() {
        // Arrange
        course2 = new Course();
        course2.setId(2L);

        List<Course> courses = new ArrayList<>();
        courses.add(course1);
        courses.add(course2);
        program.setCourses(courses);

        // Set up the bidirectional relationships
        course1.setPrograms(new ArrayList<>(List.of(program)));
        course2.setPrograms(new ArrayList<>(List.of(program)));
        // Act
        programCourseServiceImp.removeAllCoursesFromProgram(program);

        // Verify that the courses were updated to remove the program
        assertFalse(course1.getPrograms().contains(program));
        assertFalse(course2.getPrograms().contains(program));

        // Verify that the saveAll method was called to save the courses
        verify(courseRepo).saveAll(anyList());

        // Verify that the program's courses were cleared
        assertTrue(program.getCourses().isEmpty());

        // Verify that the program repository was called to save the program
        verify(programRepo).save(program);
    }

    @Test
    public void testRemoveAllCoursesFromProgram_NoCourses() {
        // Arrange
        program.setCourses(new ArrayList<>()); // Program has no courses

        // Act
        programCourseServiceImp.removeAllCoursesFromProgram(program);

        // Verify that saveAll was not called as there were no courses
        verify(courseRepo, never()).saveAll(anyList());

        // Verify that the program's courses are still empty
        assertTrue(program.getCourses().isEmpty());
    }

    @Test
    void addCourseToProgram_ShouldThrowException_WhenUserNotAuthorized() {
        // Arrange
        doThrow(UserNotAuthorizedException.class).when(accessControlService).verifyInstitutionAccess();
        // Act & Assert
        assertThrows(UserNotAuthorizedException.class, () -> programCourseServiceImp.addCourseToProgram(456L, 123L));

        // Verify that no interactions with the repository happened
        verifyNoInteractions(programRepo, courseRepo);
    }

    @Test
    void addCourseToProgram_ShouldThrowException_WhenProgramNotFound() {

        // Arrange
        when(programRepo.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProgramNotFoundException.class, () -> programCourseServiceImp.addCourseToProgram(456L, 123L));

        // Verify the programRepo was called
        verify(programRepo).findById(456L);
        verifyNoInteractions(courseRepo);
    }

    @Test
    void addCourseToProgram_ShouldReturnMessage_WhenCourseAlreadyInProgram() {
        // Arrange
        when(programRepo.findById(456L)).thenReturn(Optional.of(program));
        when(courseRepo.findById(123L)).thenReturn(Optional.of(course1));
        program.getCourses().add(course1); // Course already in program

        // Act
        MessageDTO message = programCourseServiceImp.addCourseToProgram(456L, 123L);

        // Assert
        assertEquals("Course with id: 123 is already in program with id: 456", message.message());


    }

    @Test
    void addCourseToProgram_ShouldAddCourseToProgramAndReturnMessage() {
        // Arrange
        when(programRepo.findById(456L)).thenReturn(Optional.of(program));
        when(courseRepo.findById(123L)).thenReturn(Optional.of(course1));

        // Act
        MessageDTO message = programCourseServiceImp.addCourseToProgram(456L, 123L);

        // Assert
        assertEquals("Successfully added course with id: 123 to program with id: 456", message.message());
        assertEquals(1, program.getCourses().size()); // Course added
        verify(programRepo).save(program);
        verify(courseRepo).save(course1);

    }

    @Test
    void removeCourseFromProgram_ShouldThrowException_WhenUserNotAuthorized() {
        // Arrange
        doThrow(UserNotAuthorizedException.class).when(accessControlService).verifyInstitutionAccess();
        // Act & Assert
        assertThrows(UserNotAuthorizedException.class, () -> programCourseServiceImp.removeCourseFromProgram(456L, 123L));

        // Verify that no interactions with the repository happened
        verifyNoInteractions(programRepo, courseRepo);

    }

    @Test
    void removeCourseFromProgram_ShouldThrowException_WhenProgramNotFound() {
        // Arrange
        when(programRepo.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProgramNotFoundException.class, () -> programCourseServiceImp.removeCourseFromProgram(456L, 123L));

        // Verify the programRepo was called
        verify(programRepo).findById(456L);
        verifyNoInteractions(courseRepo);


    }

    @Test
    void removeCourseFromProgram_ShouldReturnMessage_WhenCourseNotInProgram() {
        // Arrange
        when(programRepo.findById(456L)).thenReturn(Optional.of(program));

        // Act
        MessageDTO message = programCourseServiceImp.removeCourseFromProgram(456L, 123L);

        // Assert
        assertEquals("Course with id: 123 is not in program with id: 456", message.message());

    }

    @Test
    void removeCourseFromProgram_ShouldRemoveCourseFromProgramAndReturnMessage() {
        // Arrange
        when(programRepo.findById(456L)).thenReturn(Optional.of(program));
        when(courseRepo.findById(123L)).thenReturn(Optional.of(course1));
        program.getCourses().add(course1); // Course is in the program

        // Act
        MessageDTO message = programCourseServiceImp.removeCourseFromProgram(456L, 123L);

        // Assert
        assertEquals("Successfully removed course with id: 123 from program with id: 456", message.message());
        assertEquals(0, program.getCourses().size()); // Course removed
        verify(programRepo).save(program);
        verify(courseRepo).save(course1);


    }
}
