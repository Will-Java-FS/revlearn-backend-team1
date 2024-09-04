package com.revlearn.team1.dto.discussionBoard;

public record DiscussionBoardResponseDTO(
    Long discussionBoardId,
    Long courseId,
    String discussionBoardTitle
    )
{}