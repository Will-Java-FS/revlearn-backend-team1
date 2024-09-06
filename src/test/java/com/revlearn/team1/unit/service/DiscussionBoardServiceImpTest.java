package com.revlearn.team1.unit.service;

import com.revlearn.team1.dto.discussionPost.DiscussionPostResponseDTO;
import com.revlearn.team1.dto.discussionBoard.DiscussionBoardRequestDTO;
import com.revlearn.team1.dto.discussionBoard.DiscussionBoardResponseDTO;
import com.revlearn.team1.exceptions.DiscussionBoardNotFoundException;
import com.revlearn.team1.mapper.DiscussionBoardMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.DiscussionBoard;
import com.revlearn.team1.model.DiscussionPost;
import com.revlearn.team1.model.User;
import com.revlearn.team1.repository.DiscussionBoardRepo;
import com.revlearn.team1.service.discussionBoard.DiscussionBoardServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DiscussionBoardServiceImpTest {

    @Mock
    private DiscussionBoardRepo discussionBoardRepository;

    @Mock
    private DiscussionBoardMapper discussionBoardMapper;

    @InjectMocks
    private DiscussionBoardServiceImp discussionBoardService;

    @BeforeEach
    void setup() {MockitoAnnotations.openMocks(this);}

    @Test
    void testGetAllDiscussionBoardByCourseId() {
        //Arrange
        List<DiscussionPost> discussionPosts = new ArrayList<>();
        List<DiscussionBoard> discussionBoards = Arrays.asList(new DiscussionBoard(
                10L,
                new Course(),
                new User(),
                "discussion board title",
                "discussion board description",
                LocalDateTime.of(2024,5,1,13,30),
                LocalDateTime.of(2024,5,1,13,30),
                discussionPosts
        ), new DiscussionBoard(
                10L,
                new Course(),
                new User(),
                "discussion board title",
                "discussion board description",
                LocalDateTime.of(2024,5,1,13,30),
                LocalDateTime.of(2024,5,1,13,30),
                discussionPosts
        ));

        List<DiscussionPostResponseDTO> discussionPostDTOs = new ArrayList<>();
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

        when(discussionBoardRepository.getAllDiscussionBoardByCourseId(10L)).thenReturn(discussionBoards);
        when(discussionBoardMapper.toDto(any(DiscussionBoard.class))).thenReturn(discussionBoardResponseDTO.get(0), discussionBoardResponseDTO.get(1));

        // Act
        List<DiscussionBoardResponseDTO> result = discussionBoardService.getAllDiscussionBoardByCourseId(10L);

        // Assert
        assertEquals(result, discussionBoardResponseDTO);
        verify(discussionBoardRepository).getAllDiscussionBoardByCourseId(10L);
        verify(discussionBoardMapper, times(2)).toDto(any(DiscussionBoard.class));
    }

    @Test
    void testCreateDiscussionBoard() {
        // Arrange
        List<DiscussionPostResponseDTO> discussionPosts = new ArrayList<>();
        DiscussionBoardResponseDTO discussionBoardResponseDTO = new DiscussionBoardResponseDTO(
                10L,
                10L,
                10,
                "discussion board title",
                "discussion board description",
                discussionPosts
        );

        DiscussionBoardRequestDTO discussionBoardRequestDTO = new DiscussionBoardRequestDTO(
                10L,
                10,
                "discussion board title",
                "discussion board description"
        );

        DiscussionBoard discussionBoard = new DiscussionBoard();

        when(discussionBoardMapper.fromDto(any(DiscussionBoardRequestDTO.class))).thenReturn(discussionBoard);
        when(discussionBoardRepository.save(discussionBoard)).thenReturn(discussionBoard);
        when(discussionBoardMapper.toDto(any(DiscussionBoard.class))).thenReturn(discussionBoardResponseDTO);

        // Act
        DiscussionBoardResponseDTO result = discussionBoardService.createDiscussionBoard(discussionBoardRequestDTO);

        // Assert
        assertEquals(result, discussionBoardResponseDTO);
        verify(discussionBoardMapper).fromDto(discussionBoardRequestDTO);
        verify(discussionBoardRepository).save(discussionBoard);
        verify(discussionBoardMapper).toDto(discussionBoard);
    }

    @Test
    void testDeleteDiscussionBoardSuccess() {
        // Arrange
        DiscussionBoard discussionBoard = new DiscussionBoard();

        when(discussionBoardRepository.findById(10L)).thenReturn(Optional.of(discussionBoard));
        doNothing().when(discussionBoardRepository).deleteById(10L);

        // Act
        String result = discussionBoardService.deleteDiscussionBoard(10L);

        // Assert
        assertEquals(result, "Discussion Board with id: 10 deleted");
        verify(discussionBoardRepository).findById(10L);
        verify(discussionBoardRepository).deleteById(10L);
    }

    @Test
    void testDeleteDiscussionBoardFail() {
        // Arrange
        Long discussionBoardId = 10L;
        when(discussionBoardRepository.findById(discussionBoardId)).thenReturn(Optional.empty());

        // Act and Assert
        DiscussionBoardNotFoundException throwException = assertThrows(
                DiscussionBoardNotFoundException.class,
                () -> discussionBoardService.deleteDiscussionBoard(discussionBoardId),
                "Expected deleteDiscussionBoard() to throw DiscussionPostNotFoundException, but it didn't"
        );

        assertEquals(throwException.getMessage(), "Discussion Post with ID " + discussionBoardId + " not found.");
    }

    @Test
    void testUpdateDiscussionBoardSuccess(){
        // Arrange
        DiscussionBoard discussionBoard = new DiscussionBoard();

        DiscussionBoardRequestDTO discussionBoardRequestDTO = new DiscussionBoardRequestDTO(
                10L,
                10,
                "discussion board title",
                "discussion board description"
        );

        List<DiscussionPostResponseDTO> discussionPosts = new ArrayList<>();
        DiscussionBoardResponseDTO discussionBoardResponseDTO = new DiscussionBoardResponseDTO(
                10L,
                10L,
                1,
                "discussion board title",
                "discussion board description",
                discussionPosts
        );

        when(discussionBoardRepository.findById(eq(10L))).thenReturn(Optional.of(discussionBoard));
        when(discussionBoardRepository.save(any(DiscussionBoard.class))).thenReturn(discussionBoard);
        when(discussionBoardMapper.toDto(any(DiscussionBoard.class))).thenReturn(discussionBoardResponseDTO);

        // Act
        DiscussionBoardResponseDTO result = discussionBoardService.updateDiscussionBoard(10L, discussionBoardRequestDTO);

        // Assert
        assertEquals(result, discussionBoardResponseDTO);
        verify(discussionBoardRepository).findById(10L);
        verify(discussionBoardRepository).save(discussionBoard);
        verify(discussionBoardMapper).toDto(discussionBoard);
    }

    @Test
    void testUpdateDiscussionBoardFail(){
        // Arrange
        Long discussionBoardId = 10L;
        when(discussionBoardRepository.findById(discussionBoardId)).thenReturn(Optional.empty());

        // Act
        DiscussionBoardNotFoundException throwException = assertThrows(
                DiscussionBoardNotFoundException.class,
                () -> discussionBoardService.deleteDiscussionBoard(discussionBoardId),
                "Expected deleteDiscussionBoard() to throw DiscussionPostNotFoundException, but it didn't"
        );

        assertEquals(throwException.getMessage(), "Discussion Post with ID " + discussionBoardId + " not found.");
    }
}