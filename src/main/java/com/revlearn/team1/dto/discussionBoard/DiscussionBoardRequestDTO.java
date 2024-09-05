package com.revlearn.team1.dto.discussionBoard;

import com.revlearn.team1.model.Course;

public record DiscussionBoardRequestDTO(
        Long courseId,
        Integer userId,
        String discussionBoardTitle,
        String discussionBoardDescription
){};