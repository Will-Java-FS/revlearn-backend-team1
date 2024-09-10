package com.revlearn.team1.service.exam;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.exam.ExamReqDTO;
import com.revlearn.team1.dto.exam.ExamResDTO;
import com.revlearn.team1.model.Exam;
import com.revlearn.team1.model.ExamQuestion;

import java.util.List;

public interface ExamService {
    ExamResDTO getById(Long examId);

    ExamResDTO createExam(Long moduleId, ExamReqDTO examReqDTO);

    ExamResDTO updateExam(Long examId, ExamReqDTO examReqDTO);

    MessageDTO deleteExam(Long examId);

    List<ExamQuestion> getQuestionsByExamId(Long examId);
}
