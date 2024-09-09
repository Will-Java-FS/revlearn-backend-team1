package com.revlearn.team1.unit.controller;

import com.revlearn.team1.controller.CourseController;
import com.revlearn.team1.dto.course.request.CourseEducatorDTO;
import com.revlearn.team1.dto.course.request.CourseReqDTO;
import com.revlearn.team1.dto.course.response.CourseEducatorResDTO;
import com.revlearn.team1.dto.course.response.CourseResDTO;
import com.revlearn.team1.dto.user.UserResDTO;
import com.revlearn.team1.enums.AttendanceMethod;
import com.revlearn.team1.enums.Roles;
import com.revlearn.team1.service.course.CourseServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CourseControllerLogicTest {

    @Mock
    private CourseServiceImp courseService;

    @InjectMocks
    private CourseController courseController;
    private List<UserResDTO> educators;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        List<UserResDTO> educators = List.of(
                new UserResDTO(1, "johnDoe", "john.doe@example.com", Roles.EDUCATOR, "John", "Doe"),
                new UserResDTO(2, "janeSmith", "jane.smith@example.com", Roles.EDUCATOR, "Jane", "Smith"),
                new UserResDTO(3, "michaelBrown", "michael.brown@example.com", Roles.EDUCATOR, "Michael", "Brown")
        );
    }

    @Test
    public void testGetAllCourses() {
        // Arrange
        List<CourseResDTO> courses = Arrays.asList(
                new CourseResDTO(1L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse", "A course to test methods", 24.33F, educators),
                new CourseResDTO(2L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.IN_PERSON, "TestCourse2", "A second course to test methods", 66.54F, educators));
        when(courseService.getAll()).thenReturn(courses);

        // Act
        List<CourseResDTO> result = courseController.getAllCourses();

        // Assert
        assertEquals(courses, result);
        verify(courseService).getAll();
    }

    @Test
    public void testGetCourseById() {
        // Arrange
        CourseResDTO courseResDTO = new CourseResDTO(1L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse", "A course to test methods", 43.53F, educators);  // Initialize with actual data
        when(courseService.getById(1L)).thenReturn(courseResDTO);

        // Act
        CourseResDTO result = courseController.getCourseById(1L);

        // Assert
        assertEquals(courseResDTO, result);
        verify(courseService).getById(1L);
    }

    @Test
    public void testPostCourse() {
        // Arrange
        CourseReqDTO courseReqDTO = new CourseReqDTO(LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse", "A course to test methods", 22.33F);
        CourseResDTO courseResDTO = new CourseResDTO(1L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse", "A course to test methods", 22.33F, educators);

        when(courseService.createCourse(any(CourseReqDTO.class))).thenReturn(courseResDTO);

        // Act
        CourseResDTO result = courseController.postCourse(courseReqDTO);

        // Assert
        assertEquals(courseResDTO, result);
        verify(courseService).createCourse(courseReqDTO);
    }

    @Test
    public void testUpdateCourse() {
        // Arrange
        Long courseId = 1L;
        CourseReqDTO courseReqDTO = new CourseReqDTO(LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse", "A course to test methods", 99.32F);
        CourseResDTO courseResDTO = new CourseResDTO(1L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse", "A course to test methods", 99.32F, educators);

        when(courseService.updateCourse(any(Long.class), any(CourseReqDTO.class))).thenReturn(courseResDTO);

        // Act
        CourseResDTO result = courseController.updateCourse(courseId, courseReqDTO);

        // Assert
        assertEquals(courseResDTO, result);
        verify(courseService).updateCourse(courseId, courseReqDTO);
    }

    @Test
    public void testAddEducator() {
        // Arrange
        CourseEducatorResDTO responseDTO = new CourseEducatorResDTO("Success", 1L, 2L);
        when(courseService.addEducator(any(CourseEducatorDTO.class))).thenReturn(responseDTO);

        // Act
        CourseEducatorResDTO result = courseController.addEducator(new CourseEducatorDTO(1L, 2L));

        // Assert
        assertEquals(responseDTO, result);
        verify(courseService).addEducator(any(CourseEducatorDTO.class));
    }

    @Test
    public void testRemoveEducator() {
        // Arrange
        CourseEducatorResDTO responseDTO = new CourseEducatorResDTO("Success", 1L, 2L);
        when(courseService.removeEducator(any(CourseEducatorDTO.class))).thenReturn(responseDTO);

        // Act
        CourseEducatorResDTO result = courseController.removeEducator(new CourseEducatorDTO(1L, 2L));

        // Assert
        assertEquals(responseDTO, result);
        verify(courseService).removeEducator(any(CourseEducatorDTO.class));
    }

    @Test
    public void testDeleteCourse() {
        // Arrange
        when(courseService.deleteById(1L)).thenReturn("Successfully deleted course 1.");

        // Act
        String result = courseController.deleteCourse(1L);

        // Assert
        assertEquals("Successfully deleted course 1.", result);
        verify(courseService).deleteById(1L);
    }
}
