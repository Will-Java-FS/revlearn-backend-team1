package com.revlearn.team1.unit.mapper;

import com.revlearn.team1.dto.course.CourseDTO;
import com.revlearn.team1.enums.AttendanceMethod;
import com.revlearn.team1.mapper.CourseMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.User;
import com.revlearn.team1.repository.UserRepository;
import com.revlearn.team1.service.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseMapperTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CourseMapper courseMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToDto() {
        // Arrange
        User institution = new User();
        institution.setId(1);

        Course course = new Course();
        course.setId(1L);
        course.setInstitution(institution);
        course.setStartDate(LocalDate.of(2023, 1, 1));
        course.setEndDate(LocalDate.of(2023, 12, 31));
        course.setAttendanceMethod(AttendanceMethod.ONLINE);
        course.setName("Test Course");
        course.setDescription("Test Description");

        // Act
        CourseDTO courseDTO = courseMapper.toDto(course);

        // Assert
        assertEquals(1L, courseDTO.id());
        assertEquals(1, courseDTO.institutionId());
        assertEquals(LocalDate.of(2023, 1, 1), courseDTO.startDate());
        assertEquals(LocalDate.of(2023, 12, 31), courseDTO.endDate());
        assertEquals(AttendanceMethod.ONLINE, courseDTO.attendanceMethod());
        assertEquals("Test Course", courseDTO.name());
        assertEquals("Test Description", courseDTO.description());
    }

    @Test
    void testFromDto() {
        // Arrange
        User institution = new User();
        institution.setId(1);

        CourseDTO courseDTO = new CourseDTO(
                1L,
                1,
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 12, 31),
                AttendanceMethod.ONLINE,
                "Test Course",
                "Test Description"
        );

        when(userRepository.findById(1)).thenReturn(Optional.of(institution));

        // Act
        Course course = courseMapper.fromDto(courseDTO);

        // Assert
        assertEquals(institution, course.getInstitution());
        assertEquals(LocalDate.of(2023, 1, 1), course.getStartDate());
        assertEquals(LocalDate.of(2023, 12, 31), course.getEndDate());
        assertEquals(AttendanceMethod.ONLINE, course.getAttendanceMethod());
        assertEquals("Test Course", course.getName());
        assertEquals("Test Description", course.getDescription());

        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void testFromDto_UserNotFound() {
        // Arrange
        CourseDTO courseDTO = new CourseDTO(
                1L,
                999, // Assume this ID doesn't exist
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 12, 31),
                AttendanceMethod.ONLINE,
                "Test Course",
                "Test Description"
        );

        when(userRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            courseMapper.fromDto(courseDTO);
        });

        assertEquals("User not found with ID: 999", exception.getMessage());

        verify(userRepository, times(1)).findById(999);
    }

    @Test
    void testUpdateCourseFromDto() {
        // Arrange
        Course course = new Course();
        course.setStartDate(LocalDate.of(2023, 1, 1));
        course.setEndDate(LocalDate.of(2023, 12, 31));
        course.setName("Old Name");
        course.setDescription("Old Description");
        course.setAttendanceMethod(AttendanceMethod.ONLINE);

        CourseDTO courseDTO = new CourseDTO(
                1L,
                1,
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                AttendanceMethod.IN_PERSON,
                "New Name",
                "New Description"
        );

        // Act
        courseMapper.updateCourseFromDto(course, courseDTO);

        // Assert
        assertEquals(LocalDate.of(2024, 1, 1), course.getStartDate());
        assertEquals(LocalDate.of(2024, 12, 31), course.getEndDate());
        assertEquals("New Name", course.getName());
        assertEquals("New Description", course.getDescription());
        assertEquals(AttendanceMethod.IN_PERSON, course.getAttendanceMethod());
    }
}
