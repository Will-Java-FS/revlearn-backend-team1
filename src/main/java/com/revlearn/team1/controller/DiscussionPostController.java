package com.revlearn.team1.controller;

import com.revlearn.team1.dto.discussionPost.DiscussionPostRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revlearn.team1.dto.discussionPost.DiscussionPostResponseDTO;
import com.revlearn.team1.service.discussionPost.DiscussionPostServiceImp;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/discussion")
@RequiredArgsConstructor
public class DiscussionPostController {

    private final DiscussionPostServiceImp discussionPostService;



    @GetMapping("/{discussionPostId}")
    public ResponseEntity<DiscussionPostResponseDTO> getDiscussionById(@PathVariable Long discussionPostId) {
        DiscussionPostResponseDTO discussionPost = discussionPostService.getDiscussionById(discussionPostId);
        return new ResponseEntity<>(discussionPost, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DiscussionPostResponseDTO> postDiscussionPost(@RequestBody DiscussionPostRequestDTO discussionPostRequestDTO){
        DiscussionPostResponseDTO createdDiscussionPost = discussionPostService.postDiscussionPost(discussionPostRequestDTO);
        return new ResponseEntity<>(createdDiscussionPost, HttpStatus.CREATED);
    }

    @PutMapping("/{discussionPostId}")
    public ResponseEntity<DiscussionPostResponseDTO> updateDiscussionPost(@PathVariable Long discussionPostId, @RequestBody DiscussionPostRequestDTO discussionPostRequestDTO){
        DiscussionPostResponseDTO updatedDiscussionPost = discussionPostService.updateDiscussionPost(discussionPostId, discussionPostRequestDTO);
        return new ResponseEntity<>(updatedDiscussionPost, HttpStatus.OK);
    }

    @DeleteMapping("/{discussionPostId}")
    public ResponseEntity<String> deleteDiscussionById(@PathVariable Long discussionPostId){
        String deletedDiscussionPost = discussionPostService.deleteDiscussionById(discussionPostId);
        return new ResponseEntity<>(deletedDiscussionPost, HttpStatus.OK);
    }

}