package com.revlearn.team1.service;

import com.revlearn.team1.model.User;
import com.revlearn.team1.repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(List.of(new User()));

        List<User> result = userService.getAllUsers();
        Assert.assertEquals(List.of(new User(0, "username", "password", "email", "Student", "firstName", "lastName", LocalDateTime.of(2024, Month.AUGUST, 28, 8, 48, 18), LocalDateTime.of(2024, Month.AUGUST, 28, 8, 48, 18))), result);
    }

    @Test
    public void testCheckExisting() throws Exception {
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(createUser(username)));

        assertTrue(userService.checkExisting(username));
    }

    @Test
    public void testCreateUser() throws Exception {
        String username = "username1";
        User user = createUser(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encodeResponse");

        User result = userService.createUser(user);
        Assert.assertEquals(new User(0, "username1", "password", "email", "Student", "firstName", "lastName", LocalDateTime.of(2024, Month.AUGUST, 28, 8, 48, 18), LocalDateTime.of(2024, Month.AUGUST, 28, 8, 48, 18)), result);
    }

    @Test
    public void testLoadUserByUsername() throws Exception {
        String username = "username";
        User user = createUser(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername(username);
        Assert.assertEquals(new User(0, "username", "password", "email", "Student", "firstName", "lastName", LocalDateTime.of(2024, Month.AUGUST, 28, 8, 48, 18), LocalDateTime.of(2024, Month.AUGUST, 28, 8, 48, 18)), result);
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