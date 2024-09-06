package com.revlearn.team1.dto.discussionBoard;

public record DiscussionBoardRequestDTO(
        Long courseId,
        Integer userId,
        String discussionBoardTitle,
        String discussionBoardDescription
){};