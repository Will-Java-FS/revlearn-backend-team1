package com.revlearn.team1.controller;

import com.revlearn.team1.dto.discussionBoard.DiscussionBoardRequestDTO;
import com.revlearn.team1.dto.discussionBoard.DiscussionBoardResponseDTO;
import com.revlearn.team1.service.discussionBoard.DiscussionBoardServiceImp;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/discussion_board")
public class DiscussionBoardController {

    private final DiscussionBoardServiceImp disBoardServImp;

    public DiscussionBoardController(DiscussionBoardServiceImp disBoardServImp){
        this.disBoardServImp = disBoardServImp;
    }

    @GetMapping("/{course_id}")
    public ResponseEntity<List<DiscussionBoardResponseDTO>> getAllDiscussionBoardByCourseId(@PathVariable Long course_id){
        List<DiscussionBoardResponseDTO> listDisBoardDto = disBoardServImp.getAllDiscussionBoardByCourseId(course_id);
        return new ResponseEntity<>(listDisBoardDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DiscussionBoardResponseDTO> createDiscussionBoard(@RequestBody DiscussionBoardRequestDTO disBoardReqDto){
        DiscussionBoardResponseDTO savedDisBoardDto = disBoardServImp.createDiscussionBoard(disBoardReqDto);
        return new ResponseEntity<>(savedDisBoardDto, HttpStatus.CREATED);
    }

    @DeleteMapping("/{discussionBoardId}")
    public ResponseEntity<String> deleteDiscussionBoard(@PathVariable Long discussionBoardId){
        String deletedDisBoard = disBoardServImp.deleteDiscussionBoard(discussionBoardId);
        return new ResponseEntity<>(deletedDisBoard, HttpStatus.OK);
    }

    @PutMapping("/{discussionBoardId}")
    public ResponseEntity<DiscussionBoardResponseDTO> updateDiscussionBoard(@PathVariable Long discussionBoardId, @RequestBody DiscussionBoardRequestDTO disBoardReqDto){
        DiscussionBoardResponseDTO updatedDiscussionBoard = disBoardServImp.updateDiscussionBoard(discussionBoardId,disBoardReqDto);
        return new ResponseEntity<>(updatedDiscussionBoard, HttpStatus.OK);
    }

}