package com.revlearn.team1.unit.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.revlearn.team1.controller.UserController;
import com.revlearn.team1.enums.Roles;
import com.revlearn.team1.service.user.UserServiceImp;
import com.revlearn.team1.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.revlearn.team1.util.JwtUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserServiceImp userService; // Mock UserService

    @InjectMocks
    private UserController userController; // Inject UserService into UserController

    @Mock
    private JwtUtil jwtUtil;

    private ObjectMapper objectMapper;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Set up ObjectMapper
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Set up MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();


    }

    @Test
    public void testGetAllUsers() throws Exception {
        String temp1 = "username1";
        String temp2 = "username2";
        List<User> users = List.of(createUser(temp1), createUser(temp2));

        // Set up mock behavior
        when(userService.getAllUsers()).thenReturn(users);

        // Perform the test
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/user"));
        resultActions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));
    }


    @Test
    public void testLogin() throws Exception {
        User user = createUser("username4");
        String userJson = objectMapper.writeValueAsString(user);
        String token = "mocked_token";
        String rawPassword = "password";

        // Set up mock behavior
        when(userService.loadUserByUsername(user.getUsername())).thenReturn(user);

        when(passwordEncoder.matches(rawPassword, user.getPassword())).thenReturn(true);

        when(jwtUtil.generateToken(user)).thenReturn(token);

        // Perform the test
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson));

        // Assert
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("Bearer " + token))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    public void testRegisterUser() throws Exception {
        String username = "username3";
        User user = createUser(username);
        String userJson = objectMapper.writeValueAsString(user);
        String token = "sample-jwt-token";

        when(userService.checkExisting(user.getUsername())).thenReturn(false);
        when(userService.createUser(user)).thenReturn(user);
        when(jwtUtil.generateToken(user)).thenReturn(token);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(content().string(token));
    }



    private User createUser(String username) {
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setEmail("email");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setRole(Roles.STUDENT); // Default role
        user.setCreatedAt(LocalDateTime.of(2024, Month.AUGUST, 28, 8, 48, 18));
        user.setUpdatedAt(LocalDateTime.of(2024, Month.AUGUST, 28, 8, 48, 18));
        return user;
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme