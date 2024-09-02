package com.revlearn.team1.unit.controller;

import com.revlearn.team1.controller.CourseController;
import com.revlearn.team1.dto.course.CourseDTO;
import com.revlearn.team1.dto.course.request.CourseEducatorDTO;
import com.revlearn.team1.dto.course.response.CourseEducatorResDTO;
import com.revlearn.team1.enums.AttendanceMethod;
import com.revlearn.team1.service.CourseServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
public class CourseControllerTest2 {

    @Mock
    private CourseServiceImp courseService;

    @InjectMocks
    private CourseController courseController;

    private MockMvc mockMvc;

    @Test
    public void testGetAllCourses() throws Exception {
        // Arrange
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
        List<CourseDTO> courses = Arrays.asList(
                new CourseDTO(
                        1L,
                        null,
                        LocalDate.of(2024, 5, 27),
                        LocalDate.of(2024, 8, 27),
                        AttendanceMethod.HYBRID,
                        "TestCourse",
                        "A course to test methods",
                        null,
                        null),
                new CourseDTO(
                        2L,
                        null,
                        LocalDate.of(2024, 5, 27),
                        LocalDate.of(2024, 8, 27),
                        AttendanceMethod.IN_PERSON,
                        "TestCourse2",
                        "A second course to test methods",
                        null,
                        null));
        when(courseService.getAll()).thenReturn(courses);

        // Act & Assert
        mockMvc.perform(get("/api/v1/course/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void testGetCourseById() throws Exception {
        // Arrange
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
        CourseDTO courseDTO = new CourseDTO(
                1L,
                null,
                LocalDate.of(2024, 5, 27),
                LocalDate.of(2024, 8, 27),
                AttendanceMethod.HYBRID,
                "TestCourse",
                "A course to test methods",
                null,
                null); // Initialize with actual data
        when(courseService.getById(1L)).thenReturn(courseDTO);

        // Act & Assert
        mockMvc.perform(get("/api/v1/course/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(courseDTO.id()));
    }

    @Test
    public void testPostCourse() throws Exception {
        // Arrange
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
        CourseDTO courseDTO = new CourseDTO(
                1L,
                null,
                LocalDate.of(2024, 5, 27),
                LocalDate.of(2024, 8, 27),
                AttendanceMethod.HYBRID,
                "TestCourse",
                "A course to test methods",
                null,
                null); // Initialize with actual data
        when(courseService.createCourse(any(CourseDTO.class))).thenReturn(courseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/course/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{         \"startDate\": 1,\n" +
                                "        \"endDate\":2,\n" +
                                "        \"attendanceMethod\":\"HYBRID\",\n" +
                                "        \"name\":\"Econ\",\n" +
                                "        \"description\":\"Money and stuff\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(courseDTO.id()));
    }

    @Test
    public void testUpdateCourse() throws Exception {
        // Arrange
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
        CourseDTO courseDTO = new CourseDTO(
                1L,
                null,
                LocalDate.of(2024, 5, 27),
                LocalDate.of(2024, 8, 27),
                AttendanceMethod.HYBRID,
                "TestCourse",
                "A course to test methods",
                null,
                null); // Initialize with actual data
        when(courseService.updateCourse(any(CourseDTO.class))).thenReturn(courseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/v1/course/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{         \"startDate\": 1,\n" +
                                "        \"endDate\":2,\n" +
                                "        \"attendanceMethod\":\"HYBRID\",\n" +
                                "        \"name\":\"Econ\",\n" +
                                "        \"description\":\"Money and stuff\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(courseDTO.id()));
    }

    @Test
    public void testAddEducator() throws Exception {
        // Arrange
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
        CourseEducatorResDTO responseDTO = new CourseEducatorResDTO("Success", 1L, 2L);
        when(courseService.addEducator(any(CourseEducatorDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/course/educatorAdd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"courseId\":1, \"educatorId\":2 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    public void testRemoveEducator() throws Exception {
        // Arrange
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
        CourseEducatorResDTO responseDTO = new CourseEducatorResDTO("Success", 1L, 2L);
        when(courseService.removeEducator(any(CourseEducatorDTO.class))).thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(patch("/api/v1/course/educatorRemove")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"courseId\":1, \"educatorId\":2 }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Success"));
    }

    @Test
    public void testDeleteCourse() throws Exception {
        // Arrange
        mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
        when(courseService.deleteById(1L)).thenReturn("Successfully deleted course 1.");

        // Act & Assert
        mockMvc.perform(delete("/api/v1/course/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully deleted course 1."));
    }
}
