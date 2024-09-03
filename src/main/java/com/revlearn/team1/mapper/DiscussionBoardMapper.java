package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.discussionBoard.DiscussionBoardFullDTO;
import com.revlearn.team1.dto.discussionBoard.DiscussionBoardLightDTO;
import com.revlearn.team1.model.DiscussionBoard;
import org.springframework.stereotype.Component;

@Component
public class DiscussionBoardMapper {

    public DiscussionBoardLightDTO toDto(DiscussionBoard disBoard){

        return new DiscussionBoardLightDTO(
                disBoard.getDiscussionBoardId(),
                disBoard.getCourse().getId(),
                disBoard.getTitle()
        );
    }

    public DiscussionBoard fromDto(DiscussionBoardFullDTO disBoardDto){
        DiscussionBoard disBoard = new DiscussionBoard();
        disBoard.setDiscussionBoardId(disBoardDto.discussionBoardId());
        disBoard.setCourse(disBoardDto.course());
        disBoard.setTitle(disBoardDto.title());
        return disBoard;
    }
}