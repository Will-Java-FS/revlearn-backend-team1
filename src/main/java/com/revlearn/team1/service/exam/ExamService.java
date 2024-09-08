package com.revlearn.team1.service.exam;

import com.revlearn.team1.model.Exam;

public interface ExamService {
    Exam getById(Long examId);

    Exam createExam(Long moduleId, Exam exam);

    Exam updateExam(Long examId, Exam exam);

    String deleteExam(Long examId);
}
