package com.revlearn.team1.unit.mapper;

import com.revlearn.team1.dto.discussionPost.DiscussionPostRequestDTO;
import com.revlearn.team1.dto.discussionPost.DiscussionPostResponseDTO;
import com.revlearn.team1.mapper.DiscussionPostMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.DiscussionBoard;
import com.revlearn.team1.model.DiscussionPost;
import com.revlearn.team1.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiscussionPostMapperTest {

    @InjectMocks
    DiscussionPostMapper discussionPostMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToDto(){
        // Arrange
        DiscussionBoard discussionBoard = new DiscussionBoard();
        discussionBoard.setDiscussionBoardId(10L);

        Course course = new Course();
        course.setId(10L);

        User user = new User();
        user.setId(10);

        DiscussionPost discussionPost = new DiscussionPost();
        discussionPost.setDiscussionId(10L);
        discussionPost.setDiscussionBoard(discussionBoard);
        discussionPost.setCourse(course);
        discussionPost.setUser(user);
        discussionPost.setContent("discussion post content");
        discussionPost.setCreatedAt(LocalDateTime.of(2024,5,1,13,30));
        discussionPost.setUpdatedAt(LocalDateTime.of(2024,5,11,23,30));

        // Act
        DiscussionPostResponseDTO result = discussionPostMapper.toDto(discussionPost);

        // Assert
        assertEquals(result.discussionId(), 10L);
        assertEquals(result.discussionBoardId(), 10L);
        assertEquals(result.courseId(), 10L);
        assertEquals(result.userId(), 10);
        assertEquals(result.content(), "discussion post content");
        assertEquals(result.createdAt(), LocalDateTime.of(2024,5,1,13,30));
        assertEquals(result.updatedAt(), LocalDateTime.of(2024,5,11,23,30));
    }

    @Test
    void fromDto(){
        // Arrange
        DiscussionPostRequestDTO discussionPostRequestDTO = new DiscussionPostRequestDTO(
                10L,
                10L,
                10,
                "discussionPostRequestDTO"
        );

        // Act
        DiscussionPost result = discussionPostMapper.fromDto(discussionPostRequestDTO);

        // Assert
        assertEquals(result.getDiscussionBoard().getDiscussionBoardId(), 10L);
        assertEquals(result.getCourse().getId(), 10L);
        assertEquals(result.getUser().getId(), 10);
        assertEquals(result.getContent(), "discussionPostRequestDTO");
    }

}