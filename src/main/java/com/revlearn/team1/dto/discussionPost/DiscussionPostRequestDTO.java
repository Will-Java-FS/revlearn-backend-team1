package com.revlearn.team1.dto.discussionPost;

public record DiscussionPostRequestDTO(
        Long discussionBoardId,
        Long courseId,
        Integer userId,
        String content
){}