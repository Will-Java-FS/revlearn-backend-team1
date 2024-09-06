package com.revlearn.team1.unit.service;

import com.revlearn.team1.model.*;
import com.revlearn.team1.repository.ProgressRepo;
import com.revlearn.team1.service.ProgressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springdoc.api.OpenApiResourceNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProgressServiceTest {

    @Mock
    private ProgressRepo progressRepo;

    @InjectMocks
    private ProgressService progressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void noArgsConstructorTest() {
        Progress progress = new Progress();
        assertNotNull(progress);
    }

    @Test
    void allArgsConstructorTest() {
        User user = new User();
        Course course = new Course();
        CourseModule module = new CourseModule();
        ModulePage page = new ModulePage();
        Progress progress = new Progress(1L, true, 70.0F, course, module, page, user);

        assertEquals(1L, progress.getProgress_id());
        assertTrue(progress.getCompleted());
        assertEquals(70.0F, progress.getCompletedProgress());
        assertEquals(course, progress.getCourse());
        assertEquals(module, progress.getCourseModule());
        assertEquals(page, progress.getModulePage());
        assertEquals(user, progress.getUser());

    }

    @Test
    void lombokTest() {
        Progress progress = new Progress();
        assertNotNull(progress.toString());
        assertDoesNotThrow(() -> progress.equals(new Progress()));
    }

    @Test
    public void testProgressByUser() {
        User newStudent = new User();
        newStudent.setId(1);
        Progress progress1 = new Progress();
        progress1.setUser(newStudent);
        progress1.setCompletedProgress(50.0F);

        Progress progress2 = new Progress();
        progress2.setUser(newStudent);
        progress2.setCompletedProgress(80.0F);

        List<Progress> progressList = Arrays.asList(progress1, progress2);

        when(progressRepo.findByUser_id(1L)).thenReturn(Optional.of(progressList));

        List<Progress> result = progressService.getProgressByUser(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getUser().getId());
        assertEquals(50.0, result.get(0).getCompletedProgress(), 0.01);
        assertEquals(80.0, result.get(1).getCompletedProgress(), 0.01);
    }

    @Test
    public void testProgressByCourse() {
        Course newCourse = new Course();
        newCourse.setId(1L);
        Progress progress = new Progress();
        progress.setCourse(newCourse);
        progress.setCompletedProgress(90.0F);

        Progress newProgress = new Progress();
        newProgress.setCourse(newCourse);
        newProgress.setCompletedProgress(60.0F);

        List<Progress> progressList = Arrays.asList(progress, newProgress);

        when(progressRepo.findByCourse_id(1L)).thenReturn(Optional.of(progressList));

        List<Progress> result = progressService.getProgressByCourse(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getCourse().getId());
        assertEquals(90.0F, result.get(0).getCompletedProgress(), 0.01);
        assertEquals(60.0F, result.get(1).getCompletedProgress(), 0.01);
    }

    @Test
    public void testProgressByUserCourse() {
        User student = new User();
        student.setId(1);
        Progress progress = new Progress();
        progress.setUser(student);
        Course course = new Course();
        course.setId(2L);
        progress.setCourse(course);

        when(progressRepo.findByUser_idAndCourse_id(1L, 2L)).thenReturn(Optional.of(progress));

        Progress result = progressService.getProgressByUserCourse(1L, 2L);
        assertNotNull(result);
        assertEquals(1L, result.getUser().getId());
        assertEquals(2L, result.getCourse().getId());
    }

    @Test
    void testUpdateProgress() {
        Progress existingProgress = new Progress();
        existingProgress.setProgress_id(1L);
        existingProgress.setCompleted(false);
        existingProgress.setCompletedProgress(0.5F);

        Progress updatedProgress = new Progress();
        updatedProgress.setCompleted(true);
        updatedProgress.setCompletedProgress(1.0F);

        when(progressRepo.findById(1L)).thenReturn(Optional.of(existingProgress));
        when(progressRepo.save(any(Progress.class))).thenReturn(existingProgress);

        Progress result = progressService.updateProgress(1L, updatedProgress);

        assertNotNull(result);
        assertTrue(result.getCompleted());
        assertEquals(1.0f, result.getCompletedProgress());

        verify(progressRepo).save(existingProgress);
    }

    @Test
    void testUpdateProgressNotFound() {
        Progress updatedProgress = new Progress();
        updatedProgress.setCompleted(true);
        updatedProgress.setCompletedProgress(1.0F);

        when(progressRepo.findById(1L)).thenReturn(Optional.empty());

        OpenApiResourceNotFoundException exception = assertThrows(
                OpenApiResourceNotFoundException.class,
                () -> progressService.updateProgress(1L, updatedProgress)
        );

        assertEquals("Progress not found", exception.getMessage());

        // Verify that the save method was never called
        verify(progressRepo, never()).save(any(Progress.class));
    }
}
