package com.revlearn.team1.util;

import com.revlearn.team1.model.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private final String secretKey = "P0YfGdONKAXq8bHFO6IoIHZOhkPiNEeOi1dvnf+Ot9M=";
    private User testUser;
    private String token;

    @BeforeEach
    protected void setUp() throws Exception {

        MockitoAnnotations.openMocks(this);

        // Initialize the testUser
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("testPassword");
        testUser.setRole("Student");

        // Use reflection to set the secretKey field in JwtUtil
        Field secretKeyField = JwtUtil.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtUtil, secretKey);

        // Generate a token for the test user
        token = jwtUtil.generateToken(testUser.getUsername(), testUser.getRole());
    }

    @Test
    public void testGenerateToken() {
        assertNotNull(token);
    }

    @Test
    public void testDecodeJWT() throws Exception {
        Claims result = jwtUtil.decodeJWT(token);
        assertEquals("testUser", result.getSubject());
        assertEquals("Student", result.get("role"));
    }

    @Test
    public void testExtractUsername() throws Exception {
        String result = jwtUtil.extractUsername(token);
        assertEquals("testUser", result);
    }

    @Test
    public void testExtractRole() throws Exception {
        String result = jwtUtil.extractRole(token);
        assertEquals("Student", result);
    }

    @Test
    public void testValidateToken() {
        assertTrue(jwtUtil.validateToken(token, testUser));
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme