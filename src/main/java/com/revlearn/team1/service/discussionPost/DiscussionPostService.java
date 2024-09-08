package com.revlearn.team1.service.discussionPost;

import com.revlearn.team1.dto.discussionPost.DiscussionPostRequestDTO;
import com.revlearn.team1.dto.discussionPost.DiscussionPostResponseDTO;

public interface DiscussionPostService {

    DiscussionPostResponseDTO getDiscussionById(Long discussionId);

    DiscussionPostResponseDTO postDiscussionPost(DiscussionPostRequestDTO discussionPostRequestDTO);

    DiscussionPostResponseDTO updateDiscussionPost(Long discussionPostId, DiscussionPostRequestDTO discussionPostRequestDTO);

    String deleteDiscussionById(Long discussionPostId);
}