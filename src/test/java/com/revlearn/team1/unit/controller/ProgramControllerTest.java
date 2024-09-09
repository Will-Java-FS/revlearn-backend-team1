package com.revlearn.team1.unit.controller;

import com.revlearn.team1.controller.ProgramController;
import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.course.response.CourseResDTO;
import com.revlearn.team1.dto.program.ProgramReqDTO;
import com.revlearn.team1.dto.program.ProgramResDTO;
import com.revlearn.team1.dto.user.UserResDTO;
import com.revlearn.team1.enums.AttendanceMethod;
import com.revlearn.team1.enums.Roles;
import com.revlearn.team1.service.program.ProgramService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProgramControllerTest {

    @Mock
    private ProgramService programService;

    @InjectMocks
    private ProgramController programController;

    private MockMvc mockMvc;
    private List<UserResDTO> educators;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(programController).build();

        List<UserResDTO> educators = List.of(
                new UserResDTO(1, "johnDoe", "john.doe@example.com", Roles.EDUCATOR, "John", "Doe"),
                new UserResDTO(2, "janeSmith", "jane.smith@example.com", Roles.EDUCATOR, "Jane", "Smith"),
                new UserResDTO(3, "michaelBrown", "michael.brown@example.com", Roles.EDUCATOR, "Michael", "Brown")
        );
    }

    @Test
    void getAllPrograms_ReturnsProgramList() throws Exception {
        List<ProgramResDTO> programs = Arrays.asList(
                new ProgramResDTO(1L, "Program 1", "Description 1", "Department 1", "Degree 1", "Duration 1", "Location 1", "Format 1", 543.34F),
                new ProgramResDTO(2L, "Program 2", "Description 2", "Department 2", "Degree 2", "Duration 2", "Location 2", "Format 2", 2342.22F)
        );
        when(programService.getAllPrograms()).thenReturn(programs);

        mockMvc.perform(get("/api/v1/program"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Program 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].title").value("Program 2"));

        verify(programService).getAllPrograms();
    }

    @Test
    void getProgramById_ReturnsProgram() throws Exception {
        ProgramResDTO programResDTO = new ProgramResDTO(1L, "Program 1", "Description 1", "Department 1", "Degree 1", "Duration 1", "Location 1", "Format 1", 99.32F);
        when(programService.getProgramById(1L)).thenReturn(programResDTO);

        mockMvc.perform(get("/api/v1/program/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Program 1"));

        verify(programService).getProgramById(1L);
    }

    @Test
    void getProgramCourses_ReturnsCoursesList() throws Exception {
        List<CourseResDTO> courses = Arrays.asList(
                new CourseResDTO(1L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse1", "A course to test methods", 44.32F, educators),
                new CourseResDTO(2L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse2", "A course to test methods", 44.32F, educators)
        );

        when(programService.getProgramCourses(1L)).thenReturn(courses);

        mockMvc.perform(get("/api/v1/program/1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("TestCourse1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("TestCourse2"));

        verify(programService).getProgramCourses(1L);
    }

    @Test
    void getProgramStudents_ReturnsStudentList() throws Exception {
        List<UserResDTO> students = Arrays.asList(
                new UserResDTO(1, "John", "testEmail@email.test", Roles.STUDENT, "testFirstName", "testLastName"),
                new UserResDTO(2, "Jane", "janesmith@example.com", Roles.STUDENT, "Jane", "Smith")
        );
        when(programService.getProgramStudents(1L)).thenReturn(students);

        mockMvc.perform(get("/api/v1/program/1/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("testFirstName"))
                .andExpect(jsonPath("$[0].lastName").value("testLastName"))
                .andExpect(jsonPath("$[0].email").value("testEmail@email.test"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"))
                .andExpect(jsonPath("$[1].email").value("janesmith@example.com"));

        verify(programService).getProgramStudents(1L);
    }

    @Test
    void postProgram_CreatesProgram() throws Exception {
        ProgramReqDTO programReqDTO = new ProgramReqDTO("Program 1", "Description 1", "Department 1", "Degree 1", "Duration 1", "Location 1", "Format 1", 293.32F);
        ProgramResDTO programResDTO = new ProgramResDTO(1L, "Program 1", "Description 1", "Department 1", "Degree 1", "Duration 1", "Location 1", "Format 1", 332.43F);
        when(programService.createProgram(any(ProgramReqDTO.class))).thenReturn(programResDTO);

        mockMvc.perform(post("/api/v1/program")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Program 1\",\"description\":\"Description 1\",\"department\":\"Department 1\",\"degree\":\"Degree 1\",\"duration\":\"Duration 1\",\"location\":\"Location 1\",\"format\":\"Format 1\",\"cost\":7845.45}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Program 1"));

        verify(programService).createProgram(any(ProgramReqDTO.class));
    }

    @Test
    void updateProgram_UpdatesProgram() throws Exception {
        ProgramReqDTO programReqDTO = new ProgramReqDTO("Program 1 Updated", "Description 1 Updated", "Department 1", "Degree 1", "Duration 1", "Location 1", "Format 1", 223.32F);
        ProgramResDTO programResDTO = new ProgramResDTO(1L, "Program 1 Updated", "Description 1 Updated", "Department 1", "Degree 1", "Duration 1", "Location 1", "Format 1", 5232.32F);
        when(programService.updateProgram(eq(1L), any(ProgramReqDTO.class))).thenReturn(programResDTO);

        mockMvc.perform(put("/api/v1/program/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Program 1 Updated\",\"description\":\"Description 1 Updated\",\"department\":\"Department 1\",\"degree\":\"Degree 1\",\"duration\":\"Duration 1\",\"location\":\"Location 1\",\"format\":\"Format 1\",\"cost\":543.32}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Program 1 Updated"));

        verify(programService).updateProgram(eq(1L), any(ProgramReqDTO.class));
    }

    @Test
    void deleteProgram_DeletesProgram() throws Exception {
        mockMvc.perform(delete("/api/v1/program/1"))
                .andExpect(status().isOk());

        verify(programService).deleteProgram(1L);
    }

    @Test
    void addCourseToProgram_ShouldReturnSuccessMessage() throws Exception {
        // Arrange
        MessageDTO messageDTO = new MessageDTO("Successfully added course with id: 123 to program with id: 456");
        when(programService.addCourseToProgram(anyLong(), anyLong())).thenReturn(messageDTO);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/program/456/addCourse/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully added course with id: 123 to program with id: 456"));

        // Verify the service call
        verify(programService).addCourseToProgram(456L, 123L);
    }

    @Test
    void removeCourseFromProgram_ShouldReturnSuccessMessage() throws Exception {
        // Arrange
        MessageDTO messageDTO = new MessageDTO("Successfully removed course with id: 123 from program with id: 456");
        when(programService.removeCourseFromProgram(anyLong(), anyLong())).thenReturn(messageDTO);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/program/456/removeCourse/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully removed course with id: 123 from program with id: 456"));

        // Verify the service call
        verify(programService).removeCourseFromProgram(456L, 123L);
    }
}
