package com.revlearn.team1.dto;

import com.revlearn.team1.model.Course;
import com.revlearn.team1.model.User;

import java.time.LocalDateTime;

public record DiscussionPostDTO(
        Long discussionId,
        Course course,
        User user,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
){}