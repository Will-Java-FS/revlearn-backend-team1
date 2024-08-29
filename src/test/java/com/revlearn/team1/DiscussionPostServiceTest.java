package com.revlearn.team1;


import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.revlearn.team1.dto.DiscussionPostDTO;
import com.revlearn.team1.exceptions.DiscussionPostNotFoundException;
import com.revlearn.team1.mapper.DiscussionPostMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.DiscussionPost;
import com.revlearn.team1.model.User;
import com.revlearn.team1.repository.DiscussionPostRepo;
import com.revlearn.team1.service.DiscussionPostService;


class DiscussionPostServiceTest{

    @Mock
    private DiscussionPostRepo disRepo;

    @Mock
    private DiscussionPostMapper disMapper;

    @InjectMocks
    private DiscussionPostService disServ;

    @BeforeEach
    void setup() {MockitoAnnotations.openMocks(this);}

    @Test
    void testGetDiscussionByIdSuccess(){
        DiscussionPost disPost = new DiscussionPost();
        DiscussionPostDTO disDto = new DiscussionPostDTO(
                11L,
                new Course(),
                new User(),
                "content",
                LocalDateTime.of(2024,5,1,13,30),
                LocalDateTime.of(2024,5,1,13,30)
        );

        when(disRepo.findById(11L)).thenReturn(Optional.of(disPost));
        when(disMapper.toDto(disPost)).thenReturn(disDto);

        DiscussionPostDTO retrievedDis = disServ.getDiscussionById(11L);

        assertEquals(retrievedDis, disDto);
        verify(disRepo, times(1)).findById(11L);
        verify(disMapper, times(1)).toDto(disPost);
    }

    @Test
    void testGetDiscussionByIdFail() {
        // Arrange
        Long discussionId = 11L;
        when(disRepo.findById(discussionId)).thenReturn(Optional.empty());

        // Act and Assert
        DiscussionPostNotFoundException thrownException = assertThrows(
                DiscussionPostNotFoundException.class,
                () -> disServ.getDiscussionById(discussionId),
                "Expected getDiscussionById() to throw DiscussionPostNotFoundException, but it didn't");

        // Optionally, verify the exception message if your exception contains a
        // meaningful message
        assertEquals("Cannot find Discussion Post with ID 11", thrownException.getMessage());
    }

    @Test
    void testPostDiscussionPost(){
        DiscussionPost disPost = new DiscussionPost();
        DiscussionPostDTO disDto = new DiscussionPostDTO(
                11L,
                new Course(),
                new User(),
                "content",
                LocalDateTime.of(2024,5,1,13,30),
                LocalDateTime.of(2024,5,1,13,30)
        );

        when(disMapper.fromDto(disDto)).thenReturn(disPost);
        when(disRepo.save(disPost)).thenReturn(disPost);
        when(disMapper.toDto(disPost)).thenReturn(disDto);

        DiscussionPostDTO createdDis = disServ.postDiscussionPost(disDto);

        assertEquals(createdDis, disDto);
        verify(disMapper).fromDto(disDto);
        verify(disRepo).save(disPost);
        verify(disMapper).toDto(disPost);
    }

    @Test
    void testUpdateDiscussionPostSuccess(){
        DiscussionPost disPost = new DiscussionPost();
        DiscussionPostDTO disDto = new DiscussionPostDTO(
                11L,
                new Course(),
                new User(),
                "content",
                LocalDateTime.of(2024,5,1,13,30),
                LocalDateTime.of(2024,5,1,13,30)
        );

        when(disRepo.findById(11L)).thenReturn(Optional.of(disPost));
        when(disRepo.save(disPost)).thenReturn(disPost);
        when(disMapper.toDto(disPost)).thenReturn(disDto);

        DiscussionPostDTO updatedDis = disServ.updateDiscussionPost(11L,disDto);

        assertEquals(updatedDis, disDto);
        verify(disRepo).findById(11L);
        verify(disRepo).save(disPost);
        verify(disMapper).toDto(disPost);
    }

    @Test
    void testUpdateDiscussionPostFail() {
        // Arrange
        DiscussionPostDTO disDto = new DiscussionPostDTO(
                11L,
                new Course(),
                new User(),
                "content",
                LocalDateTime.of(2024, 5, 1, 13, 30),
                LocalDateTime.of(2024, 5, 1, 13, 30));
        when(disRepo.findById(11L)).thenReturn(Optional.empty());

        // Act & Assert
        DiscussionPostNotFoundException thrownException = assertThrows(
                DiscussionPostNotFoundException.class,
                () -> disServ.updateDiscussionPost(11L, disDto));

        assertEquals("Discussion Post with ID 11 not found.", thrownException.getMessage());
    }

    @Test
    void testDeleteDiscussionByIdSuccess(){
        DiscussionPost disPost = new DiscussionPost();

        when(disRepo.findById(11L)).thenReturn(Optional.of(disPost));
        doNothing().when(disRepo).deleteById(11L);

        String deletedDis = disServ.deleteDiscussionById(11L);

        assertEquals("Discussion post with id: 11 deleted", deletedDis);
        verify(disRepo).findById(11L);
        verify(disRepo).deleteById(11L);
    }

    @Test
    void testDeleteDiscussionByIdFail() {
        // Arrange
        Long discussionId = 11L;
        when(disRepo.findById(discussionId)).thenReturn(Optional.empty());

        // Act and Assert
        DiscussionPostNotFoundException thrownException = assertThrows(
                DiscussionPostNotFoundException.class,
                () -> disServ.deleteDiscussionById(discussionId),
                "Expected deleteDiscussionById() to throw DiscussionPostNotFoundException, but it didn't");

        assertEquals("Discussion Post with ID 11 not found.", thrownException.getMessage());
    }
}