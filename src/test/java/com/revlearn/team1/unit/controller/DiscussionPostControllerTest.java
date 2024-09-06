package com.revlearn.team1.unit.controller;

import com.revlearn.team1.controller.DiscussionPostController;
import com.revlearn.team1.dto.discussionPost.DiscussionPostRequestDTO;
import com.revlearn.team1.dto.discussionPost.DiscussionPostResponseDTO;
import com.revlearn.team1.service.discussionPost.DiscussionPostServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DiscussionPostControllerTest {

    @Mock
    private DiscussionPostServiceImp discussionPostService;

    @InjectMocks
    private DiscussionPostController discussionPostController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDiscussionById(){
        // Arrange
        DiscussionPostResponseDTO discussionPostResponseDTO = new DiscussionPostResponseDTO(
                10L,
                10L,
                10L,
                10,
                "content",
                LocalDateTime.of(2024,5,1,13,30),
                LocalDateTime.of(2024,5,1,13,30)
        );

        when(discussionPostService.getDiscussionById(10L)).thenReturn(discussionPostResponseDTO);

        // Act
        ResponseEntity<DiscussionPostResponseDTO> result = discussionPostController.getDiscussionById(10L);

        // & Assert
        assertEquals(result.getBody(), discussionPostResponseDTO);
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        verify(discussionPostService).getDiscussionById(10L);

    }

    @Test
    void testPostDiscussionPost(){
        // Arrange
        DiscussionPostRequestDTO discussionPostRequestDTO = new DiscussionPostRequestDTO(
                10L,
               10L,
                10,
                "discussionPostRequestDTO"
        );

        DiscussionPostResponseDTO discussionPostResponseDTO = new DiscussionPostResponseDTO(
                10L,
                10L,
                10L,
                10,
                "content",
                LocalDateTime.of(2024,5,1,13,30),
                LocalDateTime.of(2024,5,1,13,30)
        );

        when(discussionPostService.postDiscussionPost(any(DiscussionPostRequestDTO.class))).thenReturn(discussionPostResponseDTO);

        // Act
        ResponseEntity<DiscussionPostResponseDTO> result = discussionPostController.postDiscussionPost(discussionPostRequestDTO);

        // Assert
        assertEquals(result.getStatusCode(),HttpStatus.CREATED);
        assertEquals(result.getBody(), discussionPostResponseDTO);
        verify(discussionPostService).postDiscussionPost(discussionPostRequestDTO);
    }

    @Test
    void testUpdateDiscussionPost(){
        // Arrange
        DiscussionPostRequestDTO discussionPostRequestDTO = new DiscussionPostRequestDTO(
                10L,
                10L,
                10,
                "discussionPostRequestDTO"
        );

        DiscussionPostResponseDTO discussionPostResponseDTO = new DiscussionPostResponseDTO(
                10L,
                10L,
                10L,
                10,
                "content",
                LocalDateTime.of(2024,5,1,13,30),
                LocalDateTime.of(2024,5,1,13,30)
        );

        when(discussionPostService.updateDiscussionPost(eq(10L), any(DiscussionPostRequestDTO.class))).thenReturn(discussionPostResponseDTO);

        // Act
        ResponseEntity<DiscussionPostResponseDTO> result = discussionPostController.updateDiscussionPost(10L, discussionPostRequestDTO);

        // Assert
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(result.getBody(), discussionPostResponseDTO);
        verify(discussionPostService).updateDiscussionPost(10L, discussionPostRequestDTO);
    }

    @Test
    void testDeleteDiscussionById(){
        when(discussionPostService.deleteDiscussionById(eq(10L))).thenReturn("Discussion post with id: 10 deleted");

        ResponseEntity<String> deletedDis = discussionPostController.deleteDiscussionById(10L);

        assertEquals(HttpStatus.OK, deletedDis.getStatusCode());
        assertEquals("Discussion post with id: 10 deleted", deletedDis.getBody());
        verify(discussionPostService).deleteDiscussionById(10L);
    }
}