package com.revlearn.team1.repository;


import com.revlearn.team1.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProgressRepo extends JpaRepository<Progress, Long> {
    Optional<List<Progress>> findByUser_id(Long user_id);
    Optional<Progress> findByUser_idAndCourse_id(Long user_id, Long course_id);
    Optional<List<Progress>> findByCourse_id(Long course_id);
}
