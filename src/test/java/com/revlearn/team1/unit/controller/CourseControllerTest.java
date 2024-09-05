package com.revlearn.team1.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revlearn.team1.controller.CourseController;
import com.revlearn.team1.dto.course.CourseDTO;
import com.revlearn.team1.dto.course.request.CourseEducatorDTO;
import com.revlearn.team1.dto.course.request.CourseStudentDTO;
import com.revlearn.team1.dto.course.response.CourseEducatorResDTO;
import com.revlearn.team1.dto.course.response.CourseStudentResDTO;
import com.revlearn.team1.enums.AttendanceMethod;
import com.revlearn.team1.model.User;
import com.revlearn.team1.service.course.CourseServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class CourseControllerTest {

    @Mock
    private CourseServiceImp courseService;

    @InjectMocks
    private CourseController courseController;

    @Autowired
    private MockMvc mockMvc;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup() {
        courseController = new CourseController(courseService); // Assuming a constructor-based injection
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    public void testGetAllCourses() throws Exception {
        // Arrange
        List<CourseDTO> courses = Arrays.asList(new CourseDTO(1L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse", "A course to test methods", 239.34F), new CourseDTO(2L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.IN_PERSON, "TestCourse2", "A second course to test methods", 239.34F)); // Initialize with actual data
        when(courseService.getAll()).thenReturn(courses);

        // Act & Assert
        mockMvc.perform(get("/api/v1/course").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetCourseById() throws Exception {
        // Arrange
        CourseDTO courseDTO = new CourseDTO(1L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse", "A course to test methods", 95.43F); // Initialize with actual data
        when(courseService.getById(1L)).thenReturn(courseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/course/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(courseDTO.id()));
    }

    @Test
    public void testPostCourse() throws Exception {
        // Arrange
        CourseDTO courseDTO = new CourseDTO(1L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse", "A course to test methods", 59.23F); // Initialize with actual data
        when(courseService.createCourse(any(CourseDTO.class))).thenReturn(courseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/course").contentType(MediaType.APPLICATION_JSON).content("{         \"startDate\": 1,\n" + "        \"endDate\":2,\n" + "        \"attendanceMethod\":\"HYBRID\",\n" + "        \"name\":\"Econ\",\n" + "        \"description\":\"Money and stuff\" }")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(courseDTO.id()));
    }

    @Test
    public void testUpdateCourse() throws Exception {
        // Arrange
        CourseDTO courseDTO = new CourseDTO(1L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse", "A course to test methods", 44.32F); // Initialize with actual data
        when(courseService.updateCourse(any(CourseDTO.class))).thenReturn(courseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/v1/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"startDate\": 1,\n"
                                + "        \"endDate\":2,\n"
                                + "        \"attendanceMethod\":\"HYBRID\",\n"
                                + "        \"name\":\"Econ\",\n"
                                + "        \"description\":\"Money and stuff\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(courseDTO.id()));
    }

    @Test
    public void testAddEducator() throws Exception {
        // Arrange
        CourseEducatorResDTO responseDTO = new CourseEducatorResDTO("Success", 1L, 2L);
        when(courseService.addEducator(any(CourseEducatorDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/course/educator/add").contentType(MediaType.APPLICATION_JSON).content("{ \"courseId\":1, \"educatorId\":2 }")).andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    public void testRemoveEducator() throws Exception {
        // Arrange
        CourseEducatorResDTO responseDTO = new CourseEducatorResDTO("Success", 1L, 2L);
        when(courseService.removeEducator(any(CourseEducatorDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/course/educator/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"courseId\":1, \"educatorId\":2 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    public void testDeleteCourse() throws Exception {
        // Arrange
        when(courseService.deleteById(1L)).thenReturn("Successfully deleted course 1.");

        // Act & Assert
        mockMvc.perform(delete("/api/v1/course/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().string("Successfully deleted course 1."));
    }

    @Test
    public void testEnrollStudent() throws Exception {
        // Arrange
        CourseStudentDTO courseStudentDTO = new CourseStudentDTO(1L, 1L);
        CourseStudentResDTO courseStudentResDTO = new CourseStudentResDTO("Student enrolled successfully", 1L, 1L);

        when(courseService.enrollStudent(courseStudentDTO)).thenReturn(courseStudentResDTO);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/course/student/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(courseStudentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(courseStudentResDTO)));
    }

    @Test
    public void testWithdrawStudent() throws Exception {
        // Arrange
        CourseStudentDTO courseStudentDTO = new CourseStudentDTO(1L, 1L);
        CourseStudentResDTO courseStudentResDTO = new CourseStudentResDTO("Student withdrawn successfully", 1L, 1L);

        when(courseService.withdrawStudent(courseStudentDTO)).thenReturn(courseStudentResDTO);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/course/student/remove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(courseStudentDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(courseStudentResDTO)));
    }

    @Test
    public void testGetAllStudentsOfCourseId() throws Exception {
        // Arrange
        Long courseId = 1L;
        List<User> students = Arrays.asList(
                new User("student0"),
                new User("student1")
        );

        when(courseService.getAllStudentsOfCourseId(courseId)).thenReturn(students);

        // Act & Assert
        mockMvc.perform(get("/api/v1/course/1/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(students)));
    }

    @Test
    public void testGetAllEducatorsOfCourseId() throws Exception {
        // Arrange
        Long courseId = 1L;
        List<User> educators = Arrays.asList(
                new User("educator"),
                new User("educator1")
        );

        when(courseService.getAllEducatorsOfCourseId(courseId)).thenReturn(educators);

        // Act & Assert
        mockMvc.perform(get("/api/v1/course/1/educators")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(asJsonString(educators)));
    }
}
