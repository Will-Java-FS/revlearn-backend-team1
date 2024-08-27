package com.revlearn.team1;


import com.revlearn.team1.model.Progress;
import com.revlearn.team1.repository.ProgressRepo;
import com.revlearn.team1.service.ProgressService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProgressTest {

    @Mock
    private ProgressRepo progressRepo;

    @InjectMocks
    private ProgressService progressService;

    @Test
    public void testProgressByStudentCourse(){
        Progress progress = new Progress();
        progress.setStudent(1L);
        progress.setCourse(2L);

        when(progressRepo.findByStudent_idAndCourse_id(1L, 2L)).thenReturn(Optional.of(progress));

        Progress result = progressService.getProgressByStudentCourse(1L, 2L);
        assertNotNull(result);
        assertEquals(1L, result.getStudent());
        assertEquals(2L, result.getCourse());

    }
}
