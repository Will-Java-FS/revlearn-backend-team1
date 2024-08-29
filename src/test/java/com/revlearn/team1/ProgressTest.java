package com.revlearn.team1;


import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.Progress;
import com.revlearn.team1.model.User;
import com.revlearn.team1.repository.ProgressRepo;
import com.revlearn.team1.service.ProgressService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cglib.core.Local;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProgressTest {

    @Mock
    private ProgressRepo progressRepo;

    @InjectMocks
    private ProgressService progressService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void noArgsConstructorTest(){
        Progress progress = new Progress();
        assertNotNull(progress);
    }

    @Test
    void allArgsConstructorTest(){
        User student = new User();
        Course course = new Course();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime updated = LocalDateTime.now();
        Progress progress = new Progress(1L, student, course, 80.0F, created, updated);

        assertEquals(1L, progress.getProgress_id());
        assertEquals(student, progress.getStudent());
        assertEquals(course, progress.getCourse());
        assertEquals(80.0F, progress.getProgressPercentage());
        assertEquals(created, progress.getCreatedAt());
        assertEquals(updated, progress.getUpdatedAt());
    }

    @Test
    void lombokTest(){
        Progress progress = new Progress();
        assertNotNull(progress.toString());
        assertDoesNotThrow(() -> progress.equals(new Progress()));
    }

    @Test
    public void testProgressByStudent(){
        User newStudent = new User();
        newStudent.setId(1L);
        Progress progress1 = new Progress();
        progress1.setStudent(newStudent);
        progress1.setProgressPercentage(50.0F);

        Progress progress2 = new Progress();
        progress2.setStudent(newStudent);
        progress2.setProgressPercentage(80.0F);

        List<Progress> progressList = Arrays.asList(progress1, progress2);

        when(progressRepo.findByStudent_id(1L)).thenReturn(Optional.of(progressList));

        List<Progress> result = progressService.getProgressByStudent(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getStudent().getId());
        assertEquals(50.0, result.get(0).getProgressPercentage(), 0.01);
        assertEquals(80.0, result.get(1).getProgressPercentage(), 0.01);
    }

    @Test
    public void testProgressByStudentCourse(){
        User student = new User();
        student.setId(1L);
        Progress progress = new Progress();
        progress.setStudent(student);
        Course course = new Course();
        course.setId(2L);
        progress.setCourse(course);

        when(progressRepo.findByStudent_idAndCourse_id(1L, 2L)).thenReturn(Optional.of(progress));

        Progress result = progressService.getProgressByStudentCourse(1L, 2L);
        assertNotNull(result);
        assertEquals(1L, result.getStudent().getId());
        assertEquals(2L, result.getCourse().getId());
    }

}
