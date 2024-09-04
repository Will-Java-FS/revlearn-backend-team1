package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.discussionBoard.DiscussionBoardRequestDTO;
import com.revlearn.team1.dto.discussionBoard.DiscussionBoardResponseDTO;
import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.DiscussionBoard;
import org.springframework.stereotype.Component;

import com.revlearn.team1.mapper.DiscussionPostMapper;

@Component
public class DiscussionBoardMapper {

    public DiscussionBoardResponseDTO toDto(DiscussionBoard discussionBoard){

        DiscussionPostMapper discussionPostMapper = new DiscussionPostMapper();

        return new DiscussionBoardResponseDTO(
                discussionBoard.getDiscussionBoardId(),
                discussionBoard.getCourse().getId(),
                discussionBoard.getUserId(),
                discussionBoard.getTitle(),
                discussionBoard.getDescription(),
                discussionBoard.getDiscussionPosts().stream().map(discussionPostMapper::toDto).toList()
        );
    }

    public DiscussionBoard fromDto(DiscussionBoardRequestDTO discussionBoardRequestDTO){
        DiscussionBoard disBoard = new DiscussionBoard();
        Course course = new Course();
        course.setId(discussionBoardRequestDTO.courseId());

        disBoard.setDiscussionBoardId(discussionBoardRequestDTO.discussionBoardId());
        disBoard.setCourse(course);
        disBoard.setUserId(discussionBoardRequestDTO.userId());
        disBoard.setTitle(discussionBoardRequestDTO.discussionBoardTitle());
        disBoard.setDescription(discussionBoardRequestDTO.discussionBoardDescription());
        return disBoard;
    }
}