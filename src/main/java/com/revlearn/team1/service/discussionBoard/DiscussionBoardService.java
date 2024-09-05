package com.revlearn.team1.service.discussionBoard;

import com.revlearn.team1.dto.discussionBoard.DiscussionBoardResponseDTO;
import com.revlearn.team1.dto.discussionBoard.DiscussionBoardRequestDTO;

import java.util.List;

public interface DiscussionBoardService {

    List<DiscussionBoardResponseDTO> getAllDiscussionBoardByCourseId(Long courseId);

    DiscussionBoardResponseDTO createDiscussionBoard(DiscussionBoardRequestDTO discussionBoardRequestDTO);

    String deleteDiscussionBoard(Long discussionBoardId);

    DiscussionBoardResponseDTO updateDiscussionBoard(Long discussionBoardId, DiscussionBoardRequestDTO discussionBoardRequestDTO);
}