package com.revlearn.team1.unit.service;

import com.revlearn.team1.dto.course.CourseDTO;
import com.revlearn.team1.dto.course.request.CourseEducatorDTO;
import com.revlearn.team1.dto.course.response.CourseEducatorResDTO;
import com.revlearn.team1.enums.AttendanceMethod;
import com.revlearn.team1.exceptions.CourseNotFoundException;
import com.revlearn.team1.mapper.CourseMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.User;
import com.revlearn.team1.repository.CourseRepo;
import com.revlearn.team1.repository.UserRepository;
import com.revlearn.team1.service.CourseServiceImp;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImpTest {

    @Mock
    private CourseRepo courseRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImp courseService;

    @Test
    public void testGetAllSuccess() {
        // Arrange
        List<Course> courses = Arrays.asList(new Course(), new Course());
        List<CourseDTO> courseDTOs = Arrays.asList(
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
        Mockito.when(courseRepo.findAll()).thenReturn(courses);
        Mockito.when(courseMapper.toDto(any(Course.class)))
                .thenReturn(courseDTOs.get(0), courseDTOs.get(1));

        // Act
        List<CourseDTO> result = courseService.getAll();

        // Assert
        assertEquals(courseDTOs, result);
        Mockito.verify(courseRepo).findAll();
        Mockito.verify(courseMapper, Mockito.times(2)).toDto(any(Course.class));
    }

    @Test
    public void testGetByIdSuccess() {
        // Arrange
        Course course = new Course();
        CourseDTO courseDTO = new CourseDTO(
                1L,
                null,
                LocalDate.of(2024, 5, 27),
                LocalDate.of(2024, 8, 27),
                AttendanceMethod.HYBRID,
                "TestCourse",
                "A course to test methods",
                null,
                null);
        Mockito.when(courseRepo.findById(1L)).thenReturn(Optional.of(course));
        Mockito.when(courseMapper.toDto(course)).thenReturn(courseDTO);

        // Act
        CourseDTO result = courseService.getById(1L);

        // Assert
        assertEquals(courseDTO, result);
        Mockito.verify(courseRepo).findById(1L);
        Mockito.verify(courseMapper).toDto(course);
    }

    @Test
    public void testGetByIdNotFound() {
        // Arrange
        Mockito.when(courseRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CourseNotFoundException.class, () -> courseService.getById(1L));
    }

    @Test
    public void testCreateCourseSuccess() {
        // Arrange
        CourseDTO courseDTO = new CourseDTO(
                1L,
                null,
                LocalDate.of(2024, 5, 27),
                LocalDate.of(2024, 8, 27),
                AttendanceMethod.HYBRID,
                "TestCourse",
                "A course to test methods",
                null,
                null);
        Course course = new Course();
        Mockito.when(courseMapper.fromDto(courseDTO)).thenReturn(course);
        Mockito.when(courseRepo.save(course)).thenReturn(course);
        Mockito.when(courseMapper.toDto(course)).thenReturn(courseDTO);

        // Act
        CourseDTO result = courseService.createCourse(courseDTO);

        // Assert
        assertEquals(courseDTO, result);
        Mockito.verify(courseRepo).save(course);
        Mockito.verify(courseMapper).fromDto(courseDTO);
        Mockito.verify(courseMapper).toDto(course);
    }

    @Test
    public void testUpdateCourseSuccess() {
        // Arrange
        CourseDTO courseDTO = new CourseDTO(
                1L,
                null,
                LocalDate.of(2024, 5, 27),
                LocalDate.of(2024, 8, 27),
                AttendanceMethod.HYBRID,
                "TestCourse",
                "A course to test methods",
                null,
                null); // Assume it has the correct ID.
        Course course = new Course();
        Mockito.when(courseRepo.findById(courseDTO.id())).thenReturn(Optional.of(course));
        Mockito.doNothing().when(courseMapper).updateCourseFromDto(course, courseDTO);
        Mockito.when(courseRepo.save(course)).thenReturn(course);
        Mockito.when(courseMapper.toDto(course)).thenReturn(courseDTO);

        // Act
        CourseDTO result = courseService.updateCourse(courseDTO);

        // Assert
        assertEquals(courseDTO, result);
        Mockito.verify(courseRepo).findById(courseDTO.id());
        Mockito.verify(courseMapper).updateCourseFromDto(course, courseDTO);
        Mockito.verify(courseRepo).save(course);
        Mockito.verify(courseMapper).toDto(course);
    }
    @Test
    public void testUpdateCourseNotFound() {
        // Arrange
        CourseDTO courseDTO = new CourseDTO(
                1L,
                null,
                LocalDate.of(2024, 5, 27),
                LocalDate.of(2024, 8, 27),
                AttendanceMethod.HYBRID,
                "TestCourse",
                "A course to test methods",
                null,
                null);
        Mockito.when(courseRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CourseNotFoundException.class, () -> courseService.updateCourse(courseDTO));
    }
    @Test
    public void testAddEducatorSuccess() {
        // Arrange
        CourseEducatorDTO dto = new CourseEducatorDTO(1L, 2L); // courseId, educatorId
        Course course = new Course();
        User educator = new User();
        Mockito.when(courseRepo.findById(1L)).thenReturn(Optional.of(course));
        Mockito.when(userRepo.findById(2)).thenReturn(Optional.of(educator));
        Mockito.when(courseRepo.save(course)).thenReturn(course);
        Mockito.when(userRepo.save(educator)).thenReturn(educator);

        // Act
        CourseEducatorResDTO result = courseService.addEducator(dto);

        // Assert
        assertEquals("Successfully added educator to course.", result.message());
        Mockito.verify(courseRepo).findById(1L);
        Mockito.verify(userRepo).findById(2);
        Mockito.verify(courseRepo).save(course);
        Mockito.verify(userRepo).save(educator);
    }

    @Test
    public void testRemoveEducatorSuccess() {
        // Arrange
        CourseEducatorDTO dto = new CourseEducatorDTO(1L, 2L); // courseId, educatorId
        Course course = new Course();
        User educator = new User();
        Mockito.when(courseRepo.findById(1L)).thenReturn(Optional.of(course));
        Mockito.when(userRepo.findById(2)).thenReturn(Optional.of(educator));
        Mockito.when(courseRepo.save(course)).thenReturn(course);
        Mockito.when(userRepo.save(educator)).thenReturn(educator);

        // Act
        CourseEducatorResDTO result = courseService.removeEducator(dto);

        // Assert
        assertEquals("Successfully removed educator from course.", result.message());
        Mockito.verify(courseRepo).findById(1L);
        Mockito.verify(userRepo).findById(2);
        Mockito.verify(courseRepo).save(course);
        Mockito.verify(userRepo).save(educator);
    }

    @Test
    public void testDeleteByIdSuccess() {
        // Arrange
        Course course = new Course();
        Mockito.when(courseRepo.findById(1L)).thenReturn(Optional.of(course));
        Mockito.doNothing().when(courseRepo).deleteById(1L);

        // Act
        String result = courseService.deleteById(1L);

        // Assert
        assertEquals("Successfully deleted course 1.", result);
        Mockito.verify(courseRepo).findById(1L);
        Mockito.verify(courseRepo).deleteById(1L);
    }

    @Test
    public void testDeleteByIdNotFound() {
        // Arrange
        Mockito.when(courseRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CourseNotFoundException.class, () -> courseService.deleteById(1L));
    }
}
