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

    private final DiscussionBoardRepo discussionBoardRepository;
    private final DiscussionBoardMapper discussionBoardMapper;

    private DiscussionBoardServiceImp(DiscussionBoardRepo discussionBoardRepository, DiscussionBoardMapper discussionBoardMapper){
        this.discussionBoardRepository = discussionBoardRepository;
        this.discussionBoardMapper = discussionBoardMapper;
    }

    public List<DiscussionBoardResponseDTO> getAllDiscussionBoardByCourseId(Long courseId){
        return discussionBoardRepository.getAllDiscussionBoardByCourseId(courseId).stream().map(discussionBoardMapper::toDto).toList();
    }

    public DiscussionBoardResponseDTO createDiscussionBoard(DiscussionBoardRequestDTO discussionBoardRequestDTO){
        DiscussionBoard discussionBoard = discussionBoardMapper.fromDto(discussionBoardRequestDTO);
        return discussionBoardMapper.toDto(discussionBoardRepository.save(discussionBoard));
    }

    public String deleteDiscussionBoard(Long discussionBoardId){
        discussionBoardRepository.findById(discussionBoardId)
                .orElseThrow(
                        () -> new DiscussionBoardNotFoundException("Discussion Post with ID " + discussionBoardId + " not found."));
        discussionBoardRepository.deleteById(discussionBoardId);
        return String.format("Discussion Board with id: %d deleted", discussionBoardId);
    }

    public DiscussionBoardResponseDTO updateDiscussionBoard(Long discussionBoardId, DiscussionBoardRequestDTO discussionBoardRequestDTO){
        DiscussionBoard discussionBoard = discussionBoardRepository.findById(discussionBoardId)
                .orElseThrow(
                        () -> new DiscussionBoardNotFoundException("Discussion Post with ID " + discussionBoardId + " not found.")
                );

        discussionBoard.setDescription(discussionBoardRequestDTO.discussionBoardDescription());
        discussionBoard.setTitle(discussionBoardRequestDTO.discussionBoardTitle());

        return discussionBoardMapper.toDto(discussionBoardRepository.save(discussionBoard));
    }
}