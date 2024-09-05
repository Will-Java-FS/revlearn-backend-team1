package com.revlearn.team1.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revlearn.team1.dto.course.CourseDTO;
import com.revlearn.team1.enums.AttendanceMethod;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.User;
import com.revlearn.team1.repository.CourseRepo;
import com.revlearn.team1.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CourseRepo courseRepo;
    @Autowired
    private UserRepository userRepo;

    private Course course;

//    @AfterEach
//    void clearDatabase() {
//        courseRepo.deleteAll();
//        userRepo.deleteAll();
//    }

    @BeforeEach
    void setUp() {
        // Initialize a sample Course for testing
        course = new Course();
        course.setName("Sample Course");
        course.setDescription("This is a sample course");
        course.setStartDate(LocalDate.now());
        course.setEndDate(LocalDate.now().plusMonths(2));
        course.setAttendanceMethod(AttendanceMethod.ONLINE);

//        User institution = new User();
//        institution.setId(1);
//        institution.setEmail("sample@example.com");
//        institution.setFirstName("Sample");
//        institution.setLastName("Institution");
//        institution.setPassword("samplePassword");
//        institution.setRole("INSTITUTION");
//        institution.setUsername("sample");
//
//        userRepo.save(institution);
//        course.setInstitution(institution);

        courseRepo.save(course);
    }

    @Test
    void getAllCourses_ShouldReturnCoursesList() throws Exception {
        // Act
        ResultActions response = mockMvc.perform(get("/api/v1/course")
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1)) // assuming the database has only 1 course
                .andExpect(jsonPath("$[0].name").value(course.getName()));
    }

    @Test
    void getCourseById_ShouldReturnCourse_WhenCourseExists() throws Exception {
        // Act
        ResultActions response = mockMvc.perform(get("/api/v1/course/{id}", course.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(course.getName()))
                .andExpect(jsonPath("$.description").value(course.getDescription()));
    }

    @Test
    void getCourseById_ShouldReturnNotFound_WhenCourseDoesNotExist() throws Exception {
        // Act
        ResultActions response = mockMvc.perform(get("/api/v1/course/{id}", 999L) // non-existing ID
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        response.andExpect(status().isNotFound());
    }

    @Test
    void postCourse_ShouldCreateNewCourse() throws Exception {
        // Arrange
        CourseDTO courseDTO = new CourseDTO(6L, LocalDate.now(), LocalDate.now().plusMonths(3), AttendanceMethod.HYBRID, "New Course", "New Description", 99.32F);

        // Act
        ResultActions response = mockMvc.perform(post("/api/v1/course")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courseDTO)));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Course"))
                .andExpect(jsonPath("$.description").value("New Description"));

        // Ensure it was saved in the DB
        assert (courseRepo.findAll().size() == 2); // since we have one course already in setup
    }

    @Test
    void updateCourse_ShouldUpdateExistingCourse() throws Exception {
        // Arrange
        course.setName("Updated Course Name");
        CourseDTO courseDTO = new CourseDTO(course.getId(), course.getStartDate(), course.getEndDate(), course.getAttendanceMethod(), course.getName(), course.getDescription(), course.getPrice());

        // Act
        ResultActions response = mockMvc.perform(put("/api/v1/course")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(courseDTO)));

        // Assert
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Course Name"));

        // Ensure it was updated in the DB
        Course updatedCourse = courseRepo.findById(course.getId()).orElseThrow();
        assert (updatedCourse.getName().equals("Updated Course Name"));
    }

    @Test
    void deleteCourse_ShouldDeleteCourse_WhenCourseExists() throws Exception {
        // Act
        ResultActions response = mockMvc.perform(delete("/api/v1/course/{id}", course.getId())
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        response.andExpect(status().isOk());

        // Ensure it was deleted from the DB
        assert (courseRepo.findById(course.getId()).isEmpty());
    }

    @Test
    void deleteCourse_ShouldReturnNotFound_WhenCourseDoesNotExist() throws Exception {
        // Act
        ResultActions response = mockMvc.perform(delete("/api/v1/course/delete/{id}", 999L) // non-existing ID
                .contentType(MediaType.APPLICATION_JSON));

        // Assert
        response.andExpect(status().isNotFound());
    }
}
