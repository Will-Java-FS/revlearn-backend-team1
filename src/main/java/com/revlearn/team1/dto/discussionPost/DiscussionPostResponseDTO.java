package com.revlearn.team1.dto.discussionPost;

import java.time.LocalDateTime;

public record DiscussionPostResponseDTO(
        Long discussionBoardId,
        Long discussionId,
        Long courseId,
        Integer userId,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
){}