package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.discussionBoard.DiscussionBoardDTO;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.DiscussionBoard;
import org.springframework.stereotype.Component;

@Component
public class DiscussionBoardMapper {

    public DiscussionBoardDTO toDto(DiscussionBoard disBoard){

        return new DiscussionBoardDTO(
                disBoard.getDiscussionBoardId(),
                disBoard.getCourse().getId(),
                disBoard.getTitle()
        );
    }

    public DiscussionBoard fromDto(DiscussionBoardDTO disBoardDto){
        DiscussionBoard disBoard = new DiscussionBoard();
        Course course = new Course();
        course.setId(disBoardDto.courseId());

        disBoard.setDiscussionBoardId(disBoardDto.discussionBoardId());
        disBoard.setCourse(course);
        disBoard.setTitle(disBoardDto.discussionBoardTitle());
        return disBoard;
    }
}