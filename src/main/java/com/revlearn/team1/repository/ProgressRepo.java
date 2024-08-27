package com.revlearn.team1.repository;

import com.revlearn.team1.model.Progress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Repository
public interface ProgressRepo extends JpaRepository<Long, Progress> {
    Optional<List<Progress>> findByStudent_id(Long student_id);
    Optional<Progress> findByStudent_idAndCourse_id(Long student_id, Long course_id);
}
