package com.revlearn.team1.repository;

import com.revlearn.team1.model.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamResultRepo extends JpaRepository <ExamResult, Long> {

    public List<ExamResult> findByUserId(int userId);
}
