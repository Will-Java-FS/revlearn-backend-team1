package com.revlearn.team1.dto;

import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.User;

import java.time.LocalDateTime;

public record DiscussionPostDTO(
        Long discussionBoardId,
        Long discussionId,
        Long courseId,
        Integer userId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
){}