package com.revlearn.team1.unit.mapper;

import com.revlearn.team1.dto.course.request.CourseReqDTO;
import com.revlearn.team1.dto.course.response.CourseResDTO;
import com.revlearn.team1.enums.AttendanceMethod;
import com.revlearn.team1.mapper.CourseMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.User;
import com.revlearn.team1.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

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
        course.setStartDate(LocalDate.of(2023, 1, 1));
        course.setEndDate(LocalDate.of(2023, 12, 31));
        course.setAttendanceMethod(AttendanceMethod.ONLINE);
        course.setName("Test Course");
        course.setDescription("Test Description");

        // Act
        CourseResDTO courseResDTO = courseMapper.toDto(course);

        // Assert
        assertEquals(1L, courseResDTO.id());
        assertEquals(LocalDate.of(2023, 1, 1), courseResDTO.startDate());
        assertEquals(LocalDate.of(2023, 12, 31), courseResDTO.endDate());
        assertEquals(AttendanceMethod.ONLINE, courseResDTO.attendanceMethod());
        assertEquals("Test Course", courseResDTO.name());
        assertEquals("Test Description", courseResDTO.description());
    }

    @Test
    void testFromReqDto() {
        // Arrange
        User institution = new User();
        institution.setId(1);

        CourseReqDTO courseReqDTO = new CourseReqDTO(
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 12, 31),
                AttendanceMethod.ONLINE,
                "Test Course",
                "Test Description",
                0.0F
        );

        when(userRepository.findById(1)).thenReturn(Optional.of(institution));

        // Act
        Course course = courseMapper.fromReqDto(courseReqDTO);

        // Assert
        assertEquals(LocalDate.of(2023, 1, 1), course.getStartDate());
        assertEquals(LocalDate.of(2023, 12, 31), course.getEndDate());
        assertEquals(AttendanceMethod.ONLINE, course.getAttendanceMethod());
        assertEquals("Test Course", course.getName());
        assertEquals("Test Description", course.getDescription());
        assertEquals(0.0F, course.getPrice());
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

        CourseReqDTO courseReqDTO = new CourseReqDTO(
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31),
                AttendanceMethod.IN_PERSON,
                "New Name",
                "New Description",
                0.0F
        );


        // Act
        courseMapper.updateCourseFromReqDto(course, courseReqDTO);

        // Assert
        assertEquals(LocalDate.of(2024, 1, 1), course.getStartDate());
        assertEquals(LocalDate.of(2024, 12, 31), course.getEndDate());
        assertEquals("New Name", course.getName());
        assertEquals("New Description", course.getDescription());
        assertEquals(AttendanceMethod.IN_PERSON, course.getAttendanceMethod());
    }
}
