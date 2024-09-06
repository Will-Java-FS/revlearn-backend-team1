package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.discussionBoard.DiscussionBoardRequestDTO;
import com.revlearn.team1.dto.discussionBoard.DiscussionBoardResponseDTO;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.DiscussionBoard;
import com.revlearn.team1.model.User;
import org.springframework.stereotype.Component;

@Component
public class DiscussionBoardMapper {

    public DiscussionBoardResponseDTO toDto(DiscussionBoard discussionBoard){

        DiscussionPostMapper discussionPostMapper = new DiscussionPostMapper();

        return new DiscussionBoardResponseDTO(
                discussionBoard.getDiscussionBoardId(),
                discussionBoard.getCourse().getId(),
                discussionBoard.getUser().getId(),
                discussionBoard.getTitle(),
                discussionBoard.getDescription(),
                discussionBoard.getDiscussionPosts().stream().map(discussionPostMapper::toDto).toList()
        );
    }

    public DiscussionBoard fromDto(DiscussionBoardRequestDTO discussionBoardRequestDTO){

        Course course = new Course();
        course.setId(discussionBoardRequestDTO.courseId());

        User user = new User();
        user.setId(discussionBoardRequestDTO.userId());

        DiscussionBoard discussionBoard = new DiscussionBoard();
        discussionBoard.setCourse(course);
        discussionBoard.setUser(user);
        discussionBoard.setTitle(discussionBoardRequestDTO.discussionBoardTitle());
        discussionBoard.setDescription(discussionBoardRequestDTO.discussionBoardDescription());

        return discussionBoard;
    }
}