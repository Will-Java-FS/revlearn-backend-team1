package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.discussionPost.DiscussionPostRequestDTO;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.DiscussionBoard;
import com.revlearn.team1.model.User;
import org.springframework.stereotype.Component;

import com.revlearn.team1.dto.discussionPost.DiscussionPostResponseDTO;
import com.revlearn.team1.model.DiscussionPost;

@Component
public class DiscussionPostMapper{

    public DiscussionPostResponseDTO toDto(DiscussionPost discussionPost){
        return new DiscussionPostResponseDTO(
                discussionPost.getDiscussionBoard().getDiscussionBoardId(),
                discussionPost.getDiscussionId(),
                discussionPost.getCourse().getId(),
                discussionPost.getUser().getId(),
                discussionPost.getContent(),
                discussionPost.getCreatedAt(),
                discussionPost.getUpdatedAt()
        );
    }

    public DiscussionPost fromDto(DiscussionPostRequestDTO discussionPostRequestDTO){
        User user = new User();
        user.setId(discussionPostRequestDTO.userId());

        Course course = new Course();
        course.setId(discussionPostRequestDTO.courseId());

        DiscussionBoard discussionBoard = new DiscussionBoard();
        discussionBoard.setDiscussionBoardId(discussionPostRequestDTO.discussionBoardId());

        DiscussionPost dis = new DiscussionPost();
        dis.setDiscussionBoard(discussionBoard);
        dis.setCourse(course);
        dis.setUser(user);
        dis.setContent(discussionPostRequestDTO.content());

        return dis;
    }
}