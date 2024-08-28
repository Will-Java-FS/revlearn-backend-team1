package com.revlearn.team1.controller;

import com.revlearn.team1.dto.DiscussionPostDTO;
import com.revlearn.team1.model.DiscussionPost;
import com.revlearn.team1.service.DiscussionPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/discussion")
@RequiredArgsConstructor
public class DiscussionPostController {

    private DiscussionPostService disServ;

    @Autowired
    public DiscussionPostController(DiscussionPostService disServ){
        this.disServ = disServ;
    }

    @GetMapping("/{id}")
    public ResponseEntity<DiscussionPostDTO> getDiscussionById(@PathVariable Long id) {
        DiscussionPostDTO getDis = disServ.getDiscussionById(id);
        return new ResponseEntity<>(getDis, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<DiscussionPostDTO> postDiscussionPost(@RequestBody DiscussionPostDTO disDto){
        DiscussionPostDTO postDis = disServ.postDiscussionPost(disDto);
        return new ResponseEntity<>(postDis, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DiscussionPostDTO> updateDiscussionPost(@PathVariable Long id, @RequestBody DiscussionPostDTO disDto){
        return disServ.updateDiscussionPost(id, disDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteDiscussionById(@PathVariable Long id){
        disServ.deleteDiscussionById(id);
        return ResponseEntity.ok("Don't worry about it");
    }

}