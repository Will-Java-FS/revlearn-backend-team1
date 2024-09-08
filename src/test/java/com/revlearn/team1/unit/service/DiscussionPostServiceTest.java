package com.revlearn.team1.unit.service;


import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.revlearn.team1.dto.discussionPost.DiscussionPostRequestDTO;
import com.revlearn.team1.dto.discussionPost.DiscussionPostResponseDTO;
import com.revlearn.team1.service.discussionPost.DiscussionPostServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.revlearn.team1.exceptions.DiscussionPostNotFoundException;
import com.revlearn.team1.mapper.DiscussionPostMapper;
import com.revlearn.team1.model.DiscussionPost;
import com.revlearn.team1.repository.DiscussionPostRepo;


class DiscussionPostServiceTest{

    @Mock
    private DiscussionPostRepo discussionPostRepository;

    @Mock
    private DiscussionPostMapper discussionPostMapper;

    @InjectMocks
    private DiscussionPostServiceImp discussionPostService;

    @BeforeEach
    void setup() {MockitoAnnotations.openMocks(this);}

    @Test
    void testGetDiscussionByIdSuccess(){
        // Arrange
        DiscussionPost disPost = new DiscussionPost();
        DiscussionPostResponseDTO discussionPostResponseDTO = new DiscussionPostResponseDTO(
                10L,
                10L,
                10L,
                10,
                "content",
                LocalDateTime.of(2024,5,1,13,30),
                LocalDateTime.of(2024,5,1,13,30)
        );

        when(discussionPostRepository.findById(11L)).thenReturn(Optional.of(disPost));
        when(discussionPostMapper.toDto(disPost)).thenReturn(discussionPostResponseDTO);

        // Act
        DiscussionPostResponseDTO result = discussionPostService.getDiscussionById(11L);

        assertEquals(result, discussionPostResponseDTO);
        verify(discussionPostRepository).findById(11L);
        verify(discussionPostMapper).toDto(disPost);
    }

    @Test
    void testGetDiscussionByIdFail() {
        // Arrange
        Long discussionId = 11L;
        when(discussionPostRepository.findById(discussionId)).thenReturn(Optional.empty());

        // Act and Assert
        DiscussionPostNotFoundException thrownException = assertThrows(
                DiscussionPostNotFoundException.class,
                () -> discussionPostService.getDiscussionById(discussionId),
                "Expected getDiscussionById() to throw DiscussionPostNotFoundException, but it didn't");

        // Optionally, verify the exception message if your exception contains a
        // meaningful message
        assertEquals("Cannot find Discussion Post with ID 11", thrownException.getMessage());
    }

    @Test
    void testPostDiscussionPost(){
        // Arrange
        DiscussionPost discussionPost = new DiscussionPost();

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

        when(discussionPostMapper.fromDto(discussionPostRequestDTO)).thenReturn(discussionPost);
        when(discussionPostRepository.save(discussionPost)).thenReturn(discussionPost);
        when(discussionPostMapper.toDto(discussionPost)).thenReturn(discussionPostResponseDTO);

        // Act
        DiscussionPostResponseDTO result = discussionPostService.postDiscussionPost(discussionPostRequestDTO);

        // Assert
        assertEquals(result, discussionPostResponseDTO);
        verify(discussionPostMapper).fromDto(discussionPostRequestDTO);
        verify(discussionPostRepository).save(discussionPost);
        verify(discussionPostMapper).toDto(discussionPost);
    }

    @Test
    void testUpdateDiscussionPostSuccess(){
        // Arrange
        DiscussionPost discussionPost = new DiscussionPost();

        DiscussionPostRequestDTO discussionPostRequestDTO = new DiscussionPostRequestDTO(
                10L,
                10L,
                10,
                "discussionPostRequestDTO"
        );

        DiscussionPostResponseDTO discussionPostResponseDTO = new DiscussionPostResponseDTO(
                10L,
                11L,
                10L,
                10,
                "content",
                LocalDateTime.of(2024,5,1,13,30),
                LocalDateTime.of(2024,5,1,13,30)
        );

        when(discussionPostRepository.findById(11L)).thenReturn(Optional.of(discussionPost));
        when(discussionPostRepository.save(discussionPost)).thenReturn(discussionPost);
        when(discussionPostMapper.toDto(discussionPost)).thenReturn(discussionPostResponseDTO);

        // Act
        DiscussionPostResponseDTO result = discussionPostService.updateDiscussionPost(11L,discussionPostRequestDTO);

        // Assert
        assertEquals(result, discussionPostResponseDTO);
        verify(discussionPostRepository).findById(11L);
        verify(discussionPostRepository).save(discussionPost);
        verify(discussionPostMapper).toDto(discussionPost);
    }

    @Test
    void testUpdateDiscussionPostFail() {
        // Arrange
        DiscussionPostRequestDTO discussionPostRequestDTO = new DiscussionPostRequestDTO(
                10L,
                10L,
                10,
                "discussionPostRequestDTO"
        );

        when(discussionPostRepository.findById(11L)).thenReturn(Optional.empty());

        // Act & Assert
        DiscussionPostNotFoundException thrownException = assertThrows(
                DiscussionPostNotFoundException.class,
                () -> discussionPostService.updateDiscussionPost(11L, discussionPostRequestDTO),
                "Expected getDiscussionById() to throw DiscussionPostNotFoundException, but it didn't");

        assertEquals("Discussion Post with ID 11 not found.", thrownException.getMessage());
    }

    @Test
    void testDeleteDiscussionByIdSuccess(){
        // Arrange
        DiscussionPost discussionPost = new DiscussionPost();

        when(discussionPostRepository.findById(11L)).thenReturn(Optional.of(discussionPost));
        doNothing().when(discussionPostRepository).deleteById(11L);

        // Act
        String result = discussionPostService.deleteDiscussionById(11L);

        // Assert
        assertEquals(result,"Discussion post with id: 11 deleted");
        verify(discussionPostRepository).findById(11L);
        verify(discussionPostRepository).deleteById(11L);
    }

    @Test
    void testDeleteDiscussionByIdFail() {
        // Arrange
        Long discussionId = 11L;
        when(discussionPostRepository.findById(discussionId)).thenReturn(Optional.empty());

        // Act and Assert
        DiscussionPostNotFoundException thrownException = assertThrows(
                DiscussionPostNotFoundException.class,
                () -> discussionPostService.deleteDiscussionById(discussionId),
                "Expected deleteDiscussionById() to throw DiscussionPostNotFoundException, but it didn't");

        assertEquals("Discussion Post with ID 11 not found.", thrownException.getMessage());
    }
}