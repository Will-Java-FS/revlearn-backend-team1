package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.discussionBoard.DiscussionBoardRequestDTO;
import com.revlearn.team1.dto.discussionBoard.DiscussionBoardResponseDTO;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.DiscussionBoard;
import org.springframework.stereotype.Component;

@Component
public class DiscussionBoardMapper {

    public DiscussionBoardResponseDTO toDto(DiscussionBoard disBoard){

        return new DiscussionBoardResponseDTO(
                disBoard.getDiscussionBoardId(),
                disBoard.getCourse().getId(),
                disBoard.getTitle()
        );
    }

    public DiscussionBoard fromDto(DiscussionBoardRequestDTO disBoardRequestDto){
        DiscussionBoard disBoard = new DiscussionBoard();
        Course course = new Course();
        course.setId(disBoardRequestDto.courseId());

        disBoard.setDiscussionBoardId(disBoardRequestDto.discussionBoardId());
        disBoard.setCourse(course);
        disBoard.setTitle(disBoardRequestDto.discussionBoardTitle());
        return disBoard;
    }
}