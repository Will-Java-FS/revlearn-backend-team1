package com.revlearn.team1.repository;

import com.revlearn.team1.model.DiscussionBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscussionBoardRepo extends JpaRepository<DiscussionBoard,Long>{

    @Query("SELECT b FROM DiscussionBoard b WHERE b.course.id = :course_id")
    List<DiscussionBoard> getAllDiscussionBoardByCourseId(@Param("course_id") Long course_id);

}