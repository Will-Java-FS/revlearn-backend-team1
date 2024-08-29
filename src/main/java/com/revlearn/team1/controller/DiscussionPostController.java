package com.revlearn.team1.controller;

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

import com.revlearn.team1.dto.DiscussionPostDTO;
import com.revlearn.team1.service.DiscussionPostService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/discussion")
@RequiredArgsConstructor
public class DiscussionPostController {

    private final DiscussionPostService disServ;

    @GetMapping("/{id}")
    public ResponseEntity<DiscussionPostDTO> getDiscussionById(@PathVariable Long id) {
        DiscussionPostDTO getDis = disServ.getDiscussionById(id);
        return new ResponseEntity<>(getDis, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DiscussionPostDTO> postDiscussionPost(@RequestBody DiscussionPostDTO disDto){
        DiscussionPostDTO postDis = disServ.postDiscussionPost(disDto);
        return new ResponseEntity<>(postDis, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscussionPostDTO> updateDiscussionPost(@PathVariable Long id, @RequestBody DiscussionPostDTO disDto){
        DiscussionPostDTO updatedDis = disServ.updateDiscussionPost(id, disDto);
        return new ResponseEntity<>(updatedDis, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDiscussionById(@PathVariable Long id){
        String deletedDis = disServ.deleteDiscussionById(id);
        return new ResponseEntity<>(deletedDis, HttpStatus.OK);
    }

}