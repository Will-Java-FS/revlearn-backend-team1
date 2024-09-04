package com.revlearn.team1.dto.discussionBoard;

public record DiscussionBoardRequestDTO(
        Long discussionBoardId,
        Long courseId,
        String discussionBoardTitle
)
{}