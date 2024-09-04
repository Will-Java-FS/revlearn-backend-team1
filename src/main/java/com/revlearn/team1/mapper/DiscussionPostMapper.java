package com.revlearn.team1.mapper;

import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.DiscussionBoard;
import com.revlearn.team1.model.User;
import org.springframework.stereotype.Component;

import com.revlearn.team1.dto.DiscussionPostDTO;
import com.revlearn.team1.model.DiscussionPost;

@Component
public class DiscussionPostMapper{

    public DiscussionPostDTO toDto(DiscussionPost dis){
        return new DiscussionPostDTO(
                dis.getDiscussionBoard().getDiscussionBoardId(),
                dis.getDiscussionId(),
                dis.getCourse().getId(),
                dis.getUser().getId(),
                dis.getContent(),
                dis.getCreatedAt(),
                dis.getUpdatedAt()
        );
    }

    public DiscussionPost fromDto(DiscussionPostDTO disDto){
        User user = new User();
        user.setId(disDto.userId());

        Course course = new Course();
        course.setId(disDto.courseId());

        DiscussionBoard discussionBoard = new DiscussionBoard();
        discussionBoard.setDiscussionBoardId(disDto.discussionBoardId());

        DiscussionPost dis = new DiscussionPost();
        dis.setDiscussionBoard(discussionBoard);
        dis.setDiscussionId(disDto.discussionId());
        dis.setCourse(course);
        dis.setUser(user);
        dis.setContent(disDto.content());
        dis.setCreatedAt(disDto.createdAt());
        dis.setUpdatedAt(disDto.updatedAt());

        return dis;
    }
}