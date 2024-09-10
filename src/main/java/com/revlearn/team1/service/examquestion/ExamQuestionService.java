package com.revlearn.team1.service.examquestion;

import com.revlearn.team1.dto.examquestion.ExamQuestionReqDTO;
import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.examquestion.ExamQuestionResDTO;

public interface ExamQuestionService {
    ExamQuestionResDTO getById(Long questionId);

    ExamQuestionResDTO createQuestion(Long examId, ExamQuestionReqDTO examQuestionReqDTO);

    ExamQuestionResDTO updateQuestion(Long questionId, ExamQuestionReqDTO examQuestionReqDTO);

    MessageDTO deleteQuestion(Long questionId);
}
