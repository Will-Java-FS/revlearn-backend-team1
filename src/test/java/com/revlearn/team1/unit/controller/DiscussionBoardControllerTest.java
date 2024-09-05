package com.revlearn.team1.unit.controller;

import com.revlearn.team1.controller.DiscussionBoardController;
import com.revlearn.team1.dto.DiscussionPostDTO;
import com.revlearn.team1.dto.discussionBoard.DiscussionBoardRequestDTO;
import com.revlearn.team1.dto.discussionBoard.DiscussionBoardResponseDTO;
import com.revlearn.team1.service.discussionBoard.DiscussionBoardServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DiscussionBoardControllerTest {

    @Mock
    DiscussionBoardServiceImp discussionBoardService;

    @InjectMocks
    DiscussionBoardController discussionBoardController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDiscussionBoardByCourseId(){
        // Arrange
        List<DiscussionPostDTO> discussionPostDTOs = new ArrayList<>();

        List<DiscussionBoardResponseDTO> discussionBoardResponseDTO = Arrays.asList(new DiscussionBoardResponseDTO(
                10L,
                10L,
                10,
                "discussion board title",
                "discussion board description",
                discussionPostDTOs
        ), new DiscussionBoardResponseDTO(
                10L,
                10L,
                10,
                "discussion board title",
                "discussion board description",
                discussionPostDTOs
        ));

        when(discussionBoardService.getAllDiscussionBoardByCourseId(10L)).thenReturn(discussionBoardResponseDTO);

        // Act
        ResponseEntity<List<DiscussionBoardResponseDTO>> result = discussionBoardController.getAllDiscussionBoardByCourseId(10L);

        // Assert
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(result.getBody(), discussionBoardResponseDTO);
        verify(discussionBoardService).getAllDiscussionBoardByCourseId(10L);
    }

    @Test
    void testCreateDiscussionBoard(){
        // Arrange
        List<DiscussionPostDTO> discussionPosts = new ArrayList<>();

        DiscussionBoardRequestDTO discussionBoardRequestDTO = new DiscussionBoardRequestDTO(
                10L,
                10L,
                1,
                "discussion board title",
                "discussion board description"
        );

        DiscussionBoardResponseDTO discussionBoardResponseDTO = new DiscussionBoardResponseDTO(
                10L,
                10L,
                10,
                "discussion board title",
                "discussion board description",
                discussionPosts
        );

        when(discussionBoardService.createDiscussionBoard(any(DiscussionBoardRequestDTO.class))).thenReturn(discussionBoardResponseDTO);

        ResponseEntity<DiscussionBoardResponseDTO> result = discussionBoardController.createDiscussionBoard(discussionBoardRequestDTO);

        assertEquals(result.getStatusCode(), HttpStatus.CREATED);
        assertEquals(result.getBody(), discussionBoardResponseDTO);
        verify(discussionBoardService).createDiscussionBoard(discussionBoardRequestDTO);
    }

    @Test
    void testDeleteDiscussionBoard(){
        when(discussionBoardService.deleteDiscussionBoard(10L)).thenReturn("Discussion Board with id: 10 deleted");

        ResponseEntity<String> result = discussionBoardController.deleteDiscussionBoard(10L);

        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(result.getBody(), "Discussion Board with id: 10 deleted");
        verify(discussionBoardService, times(1)).deleteDiscussionBoard(10L);
    }

    @Test
    void testUpdateDiscussionBoard(){
        // Arrange
        List<DiscussionPostDTO> discussionPosts = new ArrayList<>();

        DiscussionBoardRequestDTO discussionBoardRequestDTO = new DiscussionBoardRequestDTO(
                10L,
                10L,
                1,
                "discussion board title",
                "discussion board description"
        );

        DiscussionBoardResponseDTO discussionBoardResponseDTO = new DiscussionBoardResponseDTO(
                10L,
                10L,
                10,
                "discussion board title",
                "discussion board description",
                discussionPosts
        );

        when(discussionBoardService.updateDiscussionBoard(eq(10L),any(DiscussionBoardRequestDTO.class))).thenReturn(discussionBoardResponseDTO);

        // Act
        ResponseEntity<DiscussionBoardResponseDTO> result = discussionBoardController.updateDiscussionBoard(10L, discussionBoardRequestDTO);

        // Assert
        assertEquals(result.getStatusCode(), HttpStatus.OK);
        assertEquals(result.getBody(), discussionBoardResponseDTO);
        verify(discussionBoardService).updateDiscussionBoard(10L, discussionBoardRequestDTO);
    }

}