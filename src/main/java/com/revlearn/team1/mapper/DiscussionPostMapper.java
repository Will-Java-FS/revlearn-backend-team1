package com.revlearn.team1.mapper;

import org.springframework.stereotype.Component;

import com.revlearn.team1.dto.DiscussionPostDTO;
import com.revlearn.team1.model.DiscussionPost;

@Component
public class DiscussionPostMapper{

    public DiscussionPostDTO toDto(DiscussionPost dis){
        return new DiscussionPostDTO(
                dis.getDiscussionId(),
                dis.getCourse(),
                dis.getUser(),
                dis.getContent(),
                dis.getCreatedAt(),
                dis.getUpdatedAt()
        );
    }

    public DiscussionPost fromDto(DiscussionPostDTO disDto){
        DiscussionPost dis = new DiscussionPost();
        dis.setDiscussionId(disDto.discussionId());
        dis.setCourse(disDto.course());
        dis.setUser(disDto.user());
        dis.setContent(disDto.content());
        dis.setCreatedAt(disDto.createdAt());
        dis.setUpdatedAt(disDto.updatedAt());

        return dis;
    }
}