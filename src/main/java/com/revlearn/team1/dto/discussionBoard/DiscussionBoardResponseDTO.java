package com.revlearn.team1.dto.discussionBoard;

import com.revlearn.team1.dto.DiscussionPostDTO;
import com.revlearn.team1.model.DiscussionPost;

import java.util.List;

public record DiscussionBoardResponseDTO(
    Long discussionBoardId,
    Long courseId,
    Long userId,
    String discussionBoardTitle,
    String discussionBoardDescription,
    List<DiscussionPostDTO> discussionPosts
    )
{}