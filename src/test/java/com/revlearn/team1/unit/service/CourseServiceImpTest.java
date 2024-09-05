package com.revlearn.team1.unit.service;

import com.revlearn.team1.dto.course.CourseDTO;
import com.revlearn.team1.dto.course.request.CourseEducatorDTO;
import com.revlearn.team1.dto.course.request.CourseStudentDTO;
import com.revlearn.team1.dto.course.response.CourseEducatorResDTO;
import com.revlearn.team1.dto.course.response.CourseStudentResDTO;
import com.revlearn.team1.enums.AttendanceMethod;
import com.revlearn.team1.exceptions.course.CourseNotFoundException;
import com.revlearn.team1.mapper.CourseMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.User;
import com.revlearn.team1.repository.CourseRepo;
import com.revlearn.team1.repository.UserRepository;
import com.revlearn.team1.service.course.CourseServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CourseServiceImpTest {

    @Mock
    private CourseRepo courseRepo;

    @Mock
    private UserRepository userRepo;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImp courseService;

    private Course course;
    private User student;
    private User educator;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Initialize dummy data
        course = new Course();
        course.setId(1L);
        course.setStudents(new ArrayList<>());
        course.setEducators(new ArrayList<>());

        student = new User("student");
        student.setId(1);

        educator = new User("educator");
        educator.setId(2);
    }

    @Test
    public void testGetAllSuccess() {
        // Arrange
        List<Course> courses = Arrays.asList(new Course(), new Course());
        List<CourseDTO> courseDTOs = Arrays.asList(new CourseDTO(1L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse", "A course to test methods", 53.32F), new CourseDTO(2L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.IN_PERSON, "TestCourse2", "A second course to test methods", 23.66F));
        when(courseRepo.findAll()).thenReturn(courses);
        when(courseMapper.toDto(any(Course.class))).thenReturn(courseDTOs.get(0), courseDTOs.get(1));

        // Act
        List<CourseDTO> result = courseService.getAll();

        // Assert
        assertEquals(courseDTOs, result);
        verify(courseRepo).findAll();
        verify(courseMapper, Mockito.times(2)).toDto(any(Course.class));
    }

    @Test
    public void testGetAllStudentsOfCourseId_Success() {
        // Arrange
        course.getStudents().add(student);
        when(courseRepo.findById(1L)).thenReturn(Optional.of(course));

        // Act
        List<User> students = courseService.getAllStudentsOfCourseId(1L);

        // Assert
        assertEquals(1, students.size());
        assertEquals(student, students.get(0));
    }

    @Test
    public void testGetAllStudentsOfCourseId_CourseNotFound() {
        // Arrange
        when(courseRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CourseNotFoundException.class, () -> courseService.getAllStudentsOfCourseId(1L));
    }

    @Test
    public void testGetAllEducatorsOfCourseId_Success() {
        // Arrange
        course.getEducators().add(educator);
        when(courseRepo.findById(1L)).thenReturn(Optional.of(course));

        // Act
        List<User> educators = courseService.getAllEducatorsOfCourseId(1L);

        // Assert
        assertEquals(1, educators.size());
        assertEquals(educator, educators.get(0));
    }

    @Test
    public void testGetAllEducatorsOfCourseId_CourseNotFound() {
        // Arrange
        when(courseRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CourseNotFoundException.class, () -> courseService.getAllEducatorsOfCourseId(1L));
    }

    @Test
    public void testGetByIdSuccess() {
        // Arrange
//        Course course = new Course();
        CourseDTO courseDTO = new CourseDTO(1L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse", "A course to test methods", 55.22F);
        when(courseRepo.findById(1L)).thenReturn(Optional.of(course));
        when(courseMapper.toDto(course)).thenReturn(courseDTO);

        // Act
        CourseDTO result = courseService.getById(1L);

        // Assert
        assertEquals(courseDTO, result);
        verify(courseRepo).findById(1L);
        verify(courseMapper).toDto(course);
    }

    @Test
    public void testGetByIdNotFound() {
        // Arrange
        when(courseRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CourseNotFoundException.class, () -> courseService.getById(1L));
    }

    @Test
    public void testCreateCourseSuccess() {
        // Arrange
        CourseDTO courseDTO = new CourseDTO(1L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse", "A course to test methods", 99.32F);
//        Course course = new Course();
        when(courseMapper.fromDto(courseDTO)).thenReturn(course);
        when(courseRepo.save(course)).thenReturn(course);
        when(courseMapper.toDto(course)).thenReturn(courseDTO);

        // Act
        CourseDTO result = courseService.createCourse(courseDTO);

        // Assert
        assertEquals(courseDTO, result);
        verify(courseRepo).save(course);
        verify(courseMapper).fromDto(courseDTO);
        verify(courseMapper).toDto(course);
    }

    @Test
    public void testUpdateCourseSuccess() {
        // Arrange
        CourseDTO courseDTO = new CourseDTO(1L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse", "A course to test methods", 93.11F); // Assume it has the correct ID.
        System.out.println("course: " + course);
        when(courseRepo.findById(courseDTO.id())).thenReturn(Optional.of(course));
        Mockito.doNothing().when(courseMapper).updateCourseFromDto(course, courseDTO);
        when(courseRepo.save(course)).thenReturn(course);
        when(courseMapper.toDto(course)).thenReturn(courseDTO);

        // Act
        CourseDTO result = courseService.updateCourse(courseDTO);

        // Assert
        assertEquals(courseDTO, result);
        verify(courseRepo).findById(courseDTO.id());
        verify(courseMapper).updateCourseFromDto(course, courseDTO);
        verify(courseRepo).save(course);
        verify(courseMapper).toDto(course);
    }

    @Test
    public void testUpdateCourseNotFound() {
        // Arrange
        CourseDTO courseDTO = new CourseDTO(1L, LocalDate.of(2024, 5, 27), LocalDate.of(2024, 8, 27), AttendanceMethod.HYBRID, "TestCourse", "A course to test methods", 92.43F);
        when(courseRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CourseNotFoundException.class, () -> courseService.updateCourse(courseDTO));
    }

    @Test
    public void testDeleteByIdSuccess() {
        // Arrange
//        Course course = new Course();
        when(courseRepo.findById(1L)).thenReturn(Optional.of(course));
        Mockito.doNothing().when(courseRepo).deleteById(1L);

        // Act
        String result = courseService.deleteById(1L);

        // Assert
        assertEquals("Successfully deleted course 1.", result);
        verify(courseRepo).findById(1L);
        verify(courseRepo).deleteById(1L);
    }

    @Test
    public void testDeleteByIdNotFound() {
        // Arrange
        when(courseRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CourseNotFoundException.class, () -> courseService.deleteById(1L));
    }

    @Test
    public void testEnrollStudent_Success() {
        // Arrange
        CourseStudentDTO courseStudentDTO = new CourseStudentDTO(1L, 1L);
        when(courseRepo.findById(1L)).thenReturn(Optional.of(course));
        when(userRepo.findById(1)).thenReturn(Optional.of(student));
        when(courseRepo.save(course)).thenReturn(course);
        when(userRepo.save(student)).thenReturn(student);

        // Act
        CourseStudentResDTO result = courseService.enrollStudent(courseStudentDTO);

        // Assert
        assertEquals("Successfully enrolled student into course.", result.message());
        assertEquals(1L, result.courseId());
        assertEquals(1L, result.studentId());

        verify(courseRepo).save(course);
        verify(userRepo).save(student);
    }

    @Test
    public void testEnrollStudent_CourseNotFound() {
        // Arrange
        CourseStudentDTO courseStudentDTO = new CourseStudentDTO(1L, 1L);
        when(courseRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CourseNotFoundException.class, () -> courseService.enrollStudent(courseStudentDTO));
    }

    @Test
    public void testEnrollStudent_StudentNotFound() {
        // Arrange
        CourseStudentDTO courseStudentDTO = new CourseStudentDTO(1L, 1L);
        when(userRepo.findById(Math.toIntExact(courseStudentDTO.studentId()))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> courseService.enrollStudent(courseStudentDTO));
    }

    @Test
    public void testWithdrawStudent_Success() {
        // Arrange
        CourseStudentDTO courseStudentDTO = new CourseStudentDTO(1L, 1L);
        course.getStudents().add(student);
        student.getEnrolledCourses().add(course);

        when(courseRepo.findById(1L)).thenReturn(Optional.of(course));
//        when(userRepo.findById(1)).thenReturn(Optional.of(student));
        when(courseRepo.save(course)).thenReturn(course);
        when(userRepo.save(student)).thenReturn(student);

        // Act
        CourseStudentResDTO result = courseService.withdrawStudent(courseStudentDTO);

        // Assert
        assertEquals("Successfully removed student from course.", result.message());
        assertEquals(1L, result.courseId());
        assertEquals(1L, result.studentId());

        verify(courseRepo).save(course);
        verify(userRepo).save(student);
    }

    @Test
    public void testWithdrawStudent_CourseNotFound() {
        // Arrange
        CourseStudentDTO courseStudentDTO = new CourseStudentDTO(1L, 1L);
        when(courseRepo.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CourseNotFoundException.class, () -> courseService.withdrawStudent(courseStudentDTO));
    }

    @Test
    public void testWithdrawStudent_StudentNotFound() {
        // Arrange
        CourseStudentDTO courseStudentDTO = new CourseStudentDTO(1L, 1L);
        when(userRepo.findById(Math.toIntExact(courseStudentDTO.studentId()))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> courseService.withdrawStudent(courseStudentDTO));
    }

    @Test
    public void testAddEducatorSuccess() {
        // Arrange
        CourseEducatorDTO dto = new CourseEducatorDTO(1L, 2L); // courseId, educatorId
        Course course = new Course();
        User educator = new User();
        when(courseRepo.findById(1L)).thenReturn(Optional.of(course));
        when(userRepo.findById(2)).thenReturn(Optional.of(educator));
        when(courseRepo.save(course)).thenReturn(course);
        when(userRepo.save(educator)).thenReturn(educator);

        // Act
        CourseEducatorResDTO result = courseService.addEducator(dto);

        // Assert
        assertEquals("Successfully added educator to course.", result.message());
        verify(courseRepo).findById(1L);
        verify(userRepo).findById(2);
        verify(courseRepo).save(course);
        verify(userRepo).save(educator);
    }

    @Test
    public void testAddEducator_CourseNotFound() {
        //Arrange
        CourseEducatorDTO courseEducatorDTO = new CourseEducatorDTO(1L, 2L);
        when(courseRepo.findById(courseEducatorDTO.courseId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CourseNotFoundException.class, () -> courseService.addEducator(courseEducatorDTO));
    }

    @Test
    public void testRemoveEducatorSuccess() {
        // Arrange
        CourseEducatorDTO courseEducatorDTO = new CourseEducatorDTO(1L, 2L); // courseId, educatorId
        course.getEducators().add(educator);

        when(courseRepo.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepo.save(course)).thenReturn(course);
        when(userRepo.save(educator)).thenReturn(educator);

        // Act
        CourseEducatorResDTO result = courseService.removeEducator(courseEducatorDTO);

        // Assert
        assertEquals("Successfully removed educator from course.", result.message());
        verify(courseRepo).findById(1L);
        verify(courseRepo).save(course);
        verify(userRepo).save(educator);
    }

    @Test
    public void testRemoveEducator_CourseNotFound() {
        //Arrange
        CourseEducatorDTO courseEducatorDTO = new CourseEducatorDTO(1L, 2L);
        when(courseRepo.findById(courseEducatorDTO.courseId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CourseNotFoundException.class, () -> courseService.removeEducator(courseEducatorDTO));
    }
}
