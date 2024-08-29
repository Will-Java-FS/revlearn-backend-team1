package com.revlearn.team1;

import com.revlearn.team1.controller.ProgressController;
import com.revlearn.team1.model.Progress;
import com.revlearn.team1.model.User;
import com.revlearn.team1.service.ProgressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProgressControllerTest {

    @Mock
    private ProgressService progressService;

    @InjectMocks
    private ProgressController progressController;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getStudentProgressTest(){
        User student = new User();
        student.setId(1L);

        Progress progress = new Progress();
        progress.setStudent(student);
        progress.setProgressPercentage(60.0F);

        Progress progress1 = new Progress();
        progress1.setStudent(student);
        progress1.setProgressPercentage(80.0F);

        List<Progress> studentProgress = Arrays.asList(progress, progress1);

        when(progressService.getProgressByStudent(1L)).thenReturn(studentProgress);

        ResponseEntity<List<Progress>> response = progressController.getStudentProgress(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(studentProgress, response.getBody());
        verify(progressService, times(1)).getProgressByStudent(1L);

    }

    @Test
    public void getStudentCourseProgressTest(){

    }

}
