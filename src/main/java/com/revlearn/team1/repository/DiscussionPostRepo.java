package com.revlearn.team1.repository;

import com.revlearn.team1.model.DiscussionPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscussionPostRepo extends JpaRepository<DiscussionPost, Long> {
}