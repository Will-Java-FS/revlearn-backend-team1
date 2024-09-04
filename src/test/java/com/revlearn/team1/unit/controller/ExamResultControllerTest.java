package com.revlearn.team1.unit.controller;

import com.revlearn.team1.model.User;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.revlearn.team1.controller.ExamResultController;
import com.revlearn.team1.model.ExamResult;
import com.revlearn.team1.service.ExamResultService;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ExamResultControllerTest {

    @InjectMocks
    private ExamResultController examResultController;

    @Mock
    private ExamResultService examResultService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(examResultController).build();
    }

    @Test
    public void testFindAllExamResult() throws Exception {
        ExamResult e1 = setResult("t1");
        ExamResult e2 = setResult("t2");
        List<ExamResult> examResults = List.of(e1, e2);

        when(examResultService.findAllExamResult()).thenReturn(examResults);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/examresult"));
        resultActions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(examResults)));
    }

    @Test
    public void testFindByUserId() throws Exception {
        ExamResult e1 = setResult("t1");
        ExamResult e2 = setResult("t1");
        List<ExamResult> examResults = List.of(e1, e2);

        when(examResultService.findByUserId(0)).thenReturn(examResults);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/examresult/user/0"));
        resultActions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(examResults)));
    }

    public ExamResult setResult(String t)
    {
        ExamResult examResult = new ExamResult();
        examResult.setExamId(1L);
        examResult.setGrade(11.0);
        examResult.setUser(createUser(t));
        examResult.setExamDate(LocalDateTime.of(2024, Month.AUGUST, 28, 8, 48, 18));
        return examResult;
    }

    private User createUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setEmail("email");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setRole("Student"); // Default role
        user.setCreatedAt(LocalDateTime.of(2024, Month.AUGUST, 28, 8, 48, 18));
        user.setUpdatedAt(LocalDateTime.of(2024, Month.AUGUST, 28, 8, 48, 18));
        return user;
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme