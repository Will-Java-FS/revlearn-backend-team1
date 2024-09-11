package com.revlearn.team1.mapper;

import com.revlearn.team1.dto.examquestion.ExamQuestionReqDTO;
import com.revlearn.team1.dto.examquestion.ExamQuestionResDTO;
import com.revlearn.team1.model.ExamQuestion;
import org.springframework.stereotype.Component;

@Component
public class ExamQuestionMapper {
    public ExamQuestionResDTO toExamQuestionResDTO(ExamQuestion examQuestion) {
        return new ExamQuestionResDTO(
                examQuestion.getId(),
                examQuestion.getQuestion(),
                examQuestion.getOptions(),
                examQuestion.getAnswer(),
                examQuestion.getExam().getId()
        );
    }

    public ExamQuestion toExamQuestion(ExamQuestionReqDTO examQuestionReqDTO) {
        ExamQuestion q = new ExamQuestion();
        q.setQuestion(examQuestionReqDTO.question());
        q.setAnswer(examQuestionReqDTO.answer());
        q.setOptions(examQuestionReqDTO.options());

        return q;
    }
}
