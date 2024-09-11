package com.revlearn.team1.repository;

import com.revlearn.team1.model.ExamQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamQuestionRepo extends JpaRepository<ExamQuestion, Long> {
}
