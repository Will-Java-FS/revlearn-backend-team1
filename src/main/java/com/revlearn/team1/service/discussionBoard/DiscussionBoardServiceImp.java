package com.revlearn.team1.service.discussionBoard;

import com.revlearn.team1.dto.discussionBoard.DiscussionBoardRequestDTO;
import com.revlearn.team1.dto.discussionBoard.DiscussionBoardResponseDTO;
import com.revlearn.team1.exceptions.DiscussionBoardNotFoundException;
import com.revlearn.team1.mapper.DiscussionBoardMapper;
import com.revlearn.team1.model.DiscussionBoard;
import com.revlearn.team1.repository.DiscussionBoardRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussionBoardServiceImp implements DiscussionBoardService{

    private final DiscussionBoardRepo disBoardRepo;
    private final DiscussionBoardMapper disBoardMapper;

    private DiscussionBoardServiceImp(DiscussionBoardRepo disBoardRepo, DiscussionBoardMapper disBoardMapper){
        this.disBoardRepo = disBoardRepo;
        this.disBoardMapper = disBoardMapper;
    }

    public List<DiscussionBoardResponseDTO> getAllDiscussionBoardByCourseId(Long course_id){
        return disBoardRepo.getAllDiscussionBoardByCourseId(course_id).stream().map(disBoardMapper::toDto).toList();
    }

    public DiscussionBoardResponseDTO createDiscussionBoard(DiscussionBoardRequestDTO disBoardDto){
        DiscussionBoard disBoard = disBoardMapper.fromDto(disBoardDto);
        return disBoardMapper.toDto(disBoardRepo.save(disBoard));
    }

    public String deleteDiscussionBoard(Long discussionBoardId){
        disBoardRepo.findById(discussionBoardId)
                .orElseThrow(
                        () -> new DiscussionBoardNotFoundException("Discussion Post with ID " + discussionBoardId + " not found."));
        disBoardRepo.deleteById(discussionBoardId);
        return String.format("Discussion Board with id: %d deleted", discussionBoardId);
    }

    public DiscussionBoardResponseDTO updateDiscussionBoard(Long discussionBoardId, DiscussionBoardRequestDTO discussionBoardRequestDto){
        DiscussionBoard discussionBoard = disBoardRepo.findById(discussionBoardId)
                .orElseThrow(
                        () -> new DiscussionBoardNotFoundException("Discussion Post with ID " + discussionBoardId + " not found.")
                );

        discussionBoard.setDescription(discussionBoardRequestDto.discussionBoardDescription());
        discussionBoard.setTitle(discussionBoardRequestDto.discussionBoardTitle());

        return disBoardMapper.toDto(disBoardRepo.save(discussionBoard));
    }
}