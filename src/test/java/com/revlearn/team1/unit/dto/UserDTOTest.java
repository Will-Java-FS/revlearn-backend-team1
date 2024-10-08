package com.revlearn.team1.unit.dto;

import com.revlearn.team1.dto.user.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserDTOTest {
    UserDTO userDTO = new UserDTO("username", "password");

    @Test
    public void testToString() throws Exception {
        String expected = "LoginRequest{username='username', password='password'}";
        assertEquals(expected, userDTO.toString());
    }
}

//Generated with love by TestMe :) Please raise issues & feature requests at: https://weirddev.com/forum#!/testme