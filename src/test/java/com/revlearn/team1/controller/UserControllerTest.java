package com.revlearn.team1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revlearn.team1.model.User;
import com.revlearn.team1.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private User user;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {


        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        String temp1 = "username1";
        String temp2 = "username2";
        List<User> users = List.of(createUser(temp1), createUser(temp2));

        when(userService.getAllUsers()).thenReturn(users);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/users"));
        resultActions.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));
    }

    @Test
    public void testLogin() throws Exception {
        when(userService.loadUserByUsername(anyString())).thenReturn(new User(0, "username", "password", "email", "role", "firstName", "lastName", LocalDateTime.of(2024, Month.AUGUST, 28, 8, 47, 53), LocalDateTime.of(2024, Month.AUGUST, 28, 8, 47, 53)));
        when(passwordEncoder.matches(any(CharSequence.class), anyString())).thenReturn(true);

        ResponseEntity<Object> result = userController.login(new User(0, "username", "password", "email", "role", "firstName", "lastName", LocalDateTime.of(2024, Month.AUGUST, 28, 8, 47, 53), LocalDateTime.of(2024, Month.AUGUST, 28, 8, 47, 53)));
        Assert.assertEquals(new ResponseEntity<Object>("body", null, 200), result);
    }

    @Test
    public void testRegisterUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(new User(0, "username", "password", "email", "role", "firstName", "lastName", LocalDateTime.of(2024, Month.AUGUST, 28, 8, 47, 53), LocalDateTime.of(2024, Month.AUGUST, 28, 8, 47, 53)));

        ResponseEntity<?> result = userController.registerUser(new User(0, "username", "password", "email", "role", "firstName", "lastName", LocalDateTime.of(2024, Month.AUGUST, 28, 8, 47, 53), LocalDateTime.of(2024, Month.AUGUST, 28, 8, 47, 53)));
        Assert.assertEquals(new ResponseEntity<Object>(null, null, 201), result);
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