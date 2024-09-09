package com.revlearn.team1.unit.service;

import com.revlearn.team1.dto.user.UpdateUserRequest;
import com.revlearn.team1.enums.Roles;
import com.revlearn.team1.model.User;
import com.revlearn.team1.repository.UserRepository;
import com.revlearn.team1.service.user.UserServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    BCryptPasswordEncoder passwordEncoder;
    @InjectMocks
    UserServiceImp userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testCheckExisting() throws Exception {
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(createUser(username)));

        assertTrue(userService.checkExisting(username));
    }

    @Test
    public void testUpdateUser() throws Exception
    {
        // Arrange
        int userId = 1;
        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setFirstName("John");
        updateRequest.setLastName("Doe");
        updateRequest.setOldPassword("oldPassword");
        updateRequest.setNewPassword("newPassword");

        User existingUser = new User();
        existingUser.setPassword("oldEncodedPassword");
        existingUser.setFirstName("Jane");
        existingUser.setLastName("Smith");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches(any(String.class), any(String.class))).thenReturn(true);
        when(passwordEncoder.encode(any(String.class))).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // Act
        User updatedUser = userService.updateUser(userId, updateRequest);

        // Assert
        assertNotNull(updatedUser);
        assertEquals("John", updatedUser.getFirstName());
        assertEquals("Doe", updatedUser.getLastName());
        verify(userRepository).save(updatedUser);
    }

    @Test
    public void testLoadUserByUsername() throws Exception {
        String username = "username";
        User user = createUser(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        UserDetails result = userService.loadUserByUsername(username);
        assertEquals(user, result);
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