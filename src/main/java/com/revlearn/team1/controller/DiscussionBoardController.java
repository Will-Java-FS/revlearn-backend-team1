package com.revlearn.team1.controller;

import com.revlearn.team1.dto.discussionBoard.DiscussionBoardRequestDTO;
import com.revlearn.team1.dto.discussionBoard.DiscussionBoardResponseDTO;
import com.revlearn.team1.service.discussionBoard.DiscussionBoardServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/discussion_board")
public class DiscussionBoardController {

    @Autowired
    private final DiscussionBoardServiceImp discussionBoardService;

    public DiscussionBoardController(DiscussionBoardServiceImp discussionBoardService){
        this.discussionBoardService = discussionBoardService;
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<List<DiscussionBoardResponseDTO>> getAllDiscussionBoardByCourseId(@PathVariable Long courseId){
        List<DiscussionBoardResponseDTO> listDiscussionBoardDto = discussionBoardService.getAllDiscussionBoardByCourseId(courseId);
        return new ResponseEntity<>(listDiscussionBoardDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DiscussionBoardResponseDTO> createDiscussionBoard(@RequestBody DiscussionBoardRequestDTO discussionBoardRequestDTO){
        DiscussionBoardResponseDTO savedDiscussionBoardDto = discussionBoardService.createDiscussionBoard(discussionBoardRequestDTO);
        return new ResponseEntity<>(savedDiscussionBoardDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{discussionBoardId}")
    public ResponseEntity<String> deleteDiscussionBoard(@PathVariable Long discussionBoardId){
        String deletedDiscussionBoard = discussionBoardService.deleteDiscussionBoard(discussionBoardId);
        return new ResponseEntity<>(deletedDiscussionBoard, HttpStatus.OK);
    }

    @PutMapping("/{discussionBoardId}")
    public ResponseEntity<DiscussionBoardResponseDTO> updateDiscussionBoard(@PathVariable Long discussionBoardId, @RequestBody DiscussionBoardRequestDTO discussionBoardRequestDTO){
        DiscussionBoardResponseDTO updatedDiscussionBoard = discussionBoardService.updateDiscussionBoard(discussionBoardId,discussionBoardRequestDTO);
        return new ResponseEntity<>(updatedDiscussionBoard, HttpStatus.OK);
    }

}