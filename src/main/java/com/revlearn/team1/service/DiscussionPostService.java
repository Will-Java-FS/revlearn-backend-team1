package com.revlearn.team1.service;

import com.revlearn.team1.dto.DiscussionPostDTO;
import com.revlearn.team1.exceptions.DiscussionPostNotFoundException;
import com.revlearn.team1.model.DiscussionPost;
import com.revlearn.team1.mapper.DiscussionPostMapper;
import com.revlearn.team1.repository.DiscussionPostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DiscussionPostService{
    private final DiscussionPostRepo disRepo;
    private final DiscussionPostMapper disMapper;

    @Autowired
    public DiscussionPostService(DiscussionPostRepo disRepo, DiscussionPostMapper disMapper){
        this.disRepo = disRepo;
        this.disMapper = disMapper;
    }

    public DiscussionPostDTO getDiscussionById(Long courseId) {
        DiscussionPost retrievedDis = disRepo.findById(courseId).orElseThrow(() -> new DiscussionPostNotFoundException("Cannot find Discussion Post with id" + courseId));
        return disMapper.toDto(retrievedDis);
    }

    public DiscussionPostDTO postDiscussionPost(DiscussionPostDTO disDto){
        DiscussionPost dis = disMapper.fromDto(disDto);
        DiscussionPost savedDis = disRepo.save(dis);
        return disMapper.toDto(savedDis);
    }

    public ResponseEntity<DiscussionPostDTO> updateDiscussionPost(Long id, DiscussionPostDTO disDto){
        DiscussionPost updateDis = disRepo.findById(id).orElseThrow(() -> new DiscussionPostNotFoundException("Discussion Post with id" + id + "not found."));

        updateDis.setContent(disDto.content());
        updateDis.setUpdatedAt(LocalDateTime.now()); //probably will get value from front end

        DiscussionPostDTO updatedDis = disMapper.toDto(disRepo.save(updateDis));

        return ResponseEntity.ok(updatedDis);
    }

    public void deleteDiscussionById(Long id){
        DiscussionPost deleteDis = disRepo.findById(id).orElseThrow(() -> new DiscussionPostNotFoundException("Discussion Post with id" + id + "not found."));
        disRepo.deleteById(id);
    }
}