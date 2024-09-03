package com.revlearn.team1.dto.discussionBoard;

import com.revlearn.team1.model.Course;

public record DiscussionBoardFullDTO(
        Long discussionBoardId,
        Course course,
        String title
){};