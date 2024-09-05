package com.revlearn.team1.unit.controller;

import com.revlearn.team1.controller.CourseController;
import com.revlearn.team1.dto.course.CourseDTO;
import com.revlearn.team1.dto.course.request.CourseEducatorDTO;
import com.revlearn.team1.dto.course.response.CourseEducatorResDTO;
import com.revlearn.team1.enums.AttendanceMethod;
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

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCourses() {
        // Arrange
        List<CourseDTO> courses = Arrays.asList(new CourseDTO(1L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse", "A course to test methods", 24.33F), new CourseDTO(2L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.IN_PERSON, "TestCourse2", "A second course to test methods", 66.54F));
        when(courseService.getAll()).thenReturn(courses);

        // Act
        List<CourseDTO> result = courseController.getAllCourses();

        // Assert
        assertEquals(courses, result);
        verify(courseService).getAll();
    }

    @Test
    public void testGetCourseById() {
        // Arrange
        CourseDTO courseDTO = new CourseDTO(1L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse", "A course to test methods", 43.53F);  // Initialize with actual data
        when(courseService.getById(1L)).thenReturn(courseDTO);

        // Act
        CourseDTO result = courseController.getCourseById(1L);

        // Assert
        assertEquals(courseDTO, result);
        verify(courseService).getById(1L);
    }

    @Test
    public void testPostCourse() {
        // Arrange
        CourseDTO courseDTO = new CourseDTO(1L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse", "A course to test methods", 22.33F);
        when(courseService.createCourse(any(CourseDTO.class))).thenReturn(courseDTO);

        // Act
        CourseDTO result = courseController.postCourse(courseDTO);

        // Assert
        assertEquals(courseDTO, result);
        verify(courseService).createCourse(courseDTO);
    }

    @Test
    public void testUpdateCourse() {
        // Arrange
        CourseDTO courseDTO = new CourseDTO(1L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse", "A course to test methods", 99.32F);
        when(courseService.updateCourse(any(CourseDTO.class))).thenReturn(courseDTO);

        // Act
        CourseDTO result = courseController.updateCourse(courseDTO);

        // Assert
        assertEquals(courseDTO, result);
        verify(courseService).updateCourse(courseDTO);
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
