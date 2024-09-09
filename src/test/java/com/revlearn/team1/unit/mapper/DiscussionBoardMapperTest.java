package com.revlearn.team1.unit.mapper;

import com.revlearn.team1.dto.discussionBoard.DiscussionBoardRequestDTO;
import com.revlearn.team1.dto.discussionBoard.DiscussionBoardResponseDTO;
import com.revlearn.team1.dto.discussionPost.DiscussionPostResponseDTO;
import com.revlearn.team1.mapper.DiscussionBoardMapper;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.DiscussionBoard;
import com.revlearn.team1.model.DiscussionPost;
import com.revlearn.team1.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiscussionBoardMapperTest {

    @InjectMocks
    DiscussionBoardMapper discussionBoardMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToDto() {
        // Arrange
        Course course = new Course();
        course.setId(10L);

        User user = new User();
        user.setId(10);

        DiscussionBoard postDiscussionBoard = new DiscussionBoard();
        postDiscussionBoard.setDiscussionBoardId(10L);

        DiscussionPost discussionPost = new DiscussionPost();
        discussionPost.setDiscussionId(10L);
        discussionPost.setDiscussionBoard(postDiscussionBoard);
        discussionPost.setCourse(course);
        discussionPost.setUser(user);
        discussionPost.setContent("discussion post content");
        discussionPost.setCreatedAt(LocalDateTime.of(2024,5,1,13,30));
        discussionPost.setUpdatedAt(LocalDateTime.of(2024,5,11,23,30));

        DiscussionBoard discussionBoard = new DiscussionBoard();
        discussionBoard.setDiscussionBoardId(10L);
        discussionBoard.setCourse(course);
        discussionBoard.setUser(user);
        discussionBoard.setTitle("discussion board title");
        discussionBoard.setDescription("discussion board description");
        discussionBoard.setCreatedAt(LocalDateTime.of(2024,5,1,13,30));
        discussionBoard.setUpdatedAt(LocalDateTime.of(2024,5,11,23,30));
        discussionBoard.setDiscussionPosts(Arrays.asList(discussionPost,discussionPost));

        DiscussionPostResponseDTO discussionPostResponseDTO = new DiscussionPostResponseDTO(
                10L,
                10L,
                10L,
                10,
                "discussion post content",
                LocalDateTime.of(2024,5,1,13,30),
                LocalDateTime.of(2024,5,11,23,30)
        );

        // Act
        DiscussionBoardResponseDTO result = discussionBoardMapper.toDto(discussionBoard);

        // Assert
        assertEquals(result.discussionBoardId(), 10L);
        assertEquals(result.courseId(), 10L);
        assertEquals(result.userId(), 10);
        assertEquals(result.discussionBoardTitle(), "discussion board title");
        assertEquals(result.discussionBoardDescription(), "discussion board description");
        assertEquals(result.discussionPosts().get(0), discussionPostResponseDTO);
        assertEquals(result.discussionPosts().get(1), discussionPostResponseDTO);
    }

    @Test
    void testFromDto() {
        // Arrange
        DiscussionBoardRequestDTO discussionBoardRequestDTO = new DiscussionBoardRequestDTO(
                10L,
                10,
                "discussion board title",
                "discussion board description"
        );

        // Act
        DiscussionBoard result = discussionBoardMapper.fromDto(discussionBoardRequestDTO);

        // Assert
        assertEquals(result.getCourse().getId(), 10L);
        assertEquals(result.getUser().getId(), 10);
        assertEquals(result.getTitle(), "discussion board title");
        assertEquals(result.getDescription(), "discussion board description");
    }
}