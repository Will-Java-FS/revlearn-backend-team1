package com.revlearn.team1.service.discussionPost;

import com.revlearn.team1.dto.discussionPost.DiscussionPostRequestDTO;
import com.revlearn.team1.dto.discussionPost.DiscussionPostResponseDTO;
import com.revlearn.team1.exceptions.DiscussionPostNotFoundException;
import com.revlearn.team1.mapper.DiscussionPostMapper;
import com.revlearn.team1.model.DiscussionPost;
import com.revlearn.team1.repository.DiscussionPostRepo;
import org.springframework.stereotype.Service;

@Service
public class DiscussionPostServiceImp implements DiscussionPostService{
    private final DiscussionPostRepo discussionPostRepository;
    private final DiscussionPostMapper discussionPostMapper;

    public DiscussionPostServiceImp(DiscussionPostRepo discussionPostRepository, DiscussionPostMapper discussionPostMapper){
        this.discussionPostRepository = discussionPostRepository;
        this.discussionPostMapper = discussionPostMapper;
    }

    public DiscussionPostResponseDTO getDiscussionById(Long discussionId) {
        DiscussionPost retrievedDiscussionPost = discussionPostRepository.findById(discussionId).orElseThrow(
                () -> new DiscussionPostNotFoundException("Cannot find Discussion Post with ID " + discussionId));
        return discussionPostMapper.toDto(retrievedDiscussionPost);
    }

    public DiscussionPostResponseDTO postDiscussionPost(DiscussionPostRequestDTO discussionPostRequestDTO){
        DiscussionPost discussionPost = discussionPostMapper.fromDto(discussionPostRequestDTO);
        DiscussionPost createdDiscussionPost = discussionPostRepository.save(discussionPost);
        return discussionPostMapper.toDto(createdDiscussionPost);
    }

    public DiscussionPostResponseDTO updateDiscussionPost(Long discussionPostId, DiscussionPostRequestDTO discussionPostRequestDTO){
        DiscussionPost updatedDiscussionPost = discussionPostRepository.findById(discussionPostId).orElseThrow(
                () -> new DiscussionPostNotFoundException("Discussion Post with ID " + discussionPostId + " not found."));

        updatedDiscussionPost.setContent(discussionPostRequestDTO.content());

        return discussionPostMapper.toDto(discussionPostRepository.save(updatedDiscussionPost));
    }

    public String deleteDiscussionById(Long discussionPostId){
        discussionPostRepository.findById(discussionPostId)
                .orElseThrow(
                        () -> new DiscussionPostNotFoundException("Discussion Post with ID " + discussionPostId + " not found."));
        discussionPostRepository.deleteById(discussionPostId);
        return String.format("Discussion post with id: %d deleted", discussionPostId);
    }
}