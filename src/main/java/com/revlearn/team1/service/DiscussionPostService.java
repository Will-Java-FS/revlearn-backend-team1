package com.revlearn.team1.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.revlearn.team1.dto.DiscussionPostDTO;
import com.revlearn.team1.exceptions.DiscussionPostNotFoundException;
import com.revlearn.team1.mapper.DiscussionPostMapper;
import com.revlearn.team1.model.DiscussionPost;
import com.revlearn.team1.repository.DiscussionPostRepo;

@Service
public class DiscussionPostService{
    private final DiscussionPostRepo disRepo;
    private final DiscussionPostMapper disMapper;

    public DiscussionPostService(DiscussionPostRepo disRepo, DiscussionPostMapper disMapper){
        this.disRepo = disRepo;
        this.disMapper = disMapper;
    }

    public DiscussionPostDTO getDiscussionById(Long discussionId) {
        DiscussionPost retrievedDis = disRepo.findById(discussionId).orElseThrow(
                () -> new DiscussionPostNotFoundException("Cannot find Discussion Post with ID " + discussionId));
        return disMapper.toDto(retrievedDis);
    }

    public DiscussionPostDTO postDiscussionPost(DiscussionPostDTO disDto){
        DiscussionPost dis = disMapper.fromDto(disDto);
        DiscussionPost savedDis = disRepo.save(dis);
        return disMapper.toDto(savedDis);
    }

    public DiscussionPostDTO updateDiscussionPost(Long id, DiscussionPostDTO disDto){
        DiscussionPost updateDis = disRepo.findById(id).orElseThrow(
                () -> new DiscussionPostNotFoundException("Discussion Post with ID " + id + " not found."));

        updateDis.setContent(disDto.content());
        updateDis.setUpdatedAt(LocalDateTime.now()); //probably will get value from front end

        return disMapper.toDto(disRepo.save(updateDis));
    }

    public String deleteDiscussionById(Long id){
        disRepo.findById(id)
                .orElseThrow(
                        () -> new DiscussionPostNotFoundException("Discussion Post with ID " + id + " not found."));
        disRepo.deleteById(id);
        return String.format("Discussion post with id: %d deleted", id);
    }
}