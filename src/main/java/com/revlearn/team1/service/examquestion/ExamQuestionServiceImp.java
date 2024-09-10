package com.revlearn.team1.service.examquestion;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.examquestion.ExamQuestionReqDTO;
import com.revlearn.team1.dto.examquestion.ExamQuestionResDTO;
import com.revlearn.team1.exceptions.ExamNotFoundException;
import com.revlearn.team1.exceptions.QuestionNotFoundException;
import com.revlearn.team1.mapper.ExamQuestionMapper;
import com.revlearn.team1.model.Exam;
import com.revlearn.team1.model.ExamQuestion;
import com.revlearn.team1.repository.ExamQuestionRepo;
import com.revlearn.team1.repository.ExamRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamQuestionServiceImp implements ExamQuestionService {

    private final ExamQuestionRepo examQuestionRepo;
    private final ExamQuestionMapper examQuestionMapper;
    private final ExamRepo examRepo;

    @Override
    public ExamQuestionResDTO getById(Long questionId) {
        return examQuestionRepo.findById(questionId).map(examQuestionMapper::toExamQuestionResDTO).orElse(null);
    }

    @Override
    public ExamQuestionResDTO createQuestion(Long examId, ExamQuestionReqDTO examQuestionReqDTO) {
        //Verify Exam exists
        Exam exam = examRepo.findById(examId).orElseThrow(
                () -> new ExamNotFoundException(examId));

        //TODO: Verify request has authorization to create question on this exam

        //Create question
        ExamQuestion examQuestion = examQuestionMapper.toExamQuestion(examQuestionReqDTO);

        //Set exam
        examQuestion.setExam(exam);

        //save exam?  No, because question is the owner of the relationship.  I think.
//        examRepo.save(exam);

        return examQuestionMapper.toExamQuestionResDTO(examQuestionRepo.save(examQuestion));
    }

    @Override
    public ExamQuestionResDTO updateQuestion(Long questionId, ExamQuestionReqDTO examQuestionReqDTO) {
        //Verify question exists
        ExamQuestion examQuestion = examQuestionRepo.findById(questionId).orElseThrow(
                () -> new QuestionNotFoundException(questionId));

        //TODO: Verify request has authorization to update question on this exam

        //Update question
        examQuestion.setQuestion(examQuestionReqDTO.question());
        examQuestion.setOptions(examQuestionReqDTO.options());
        examQuestion.setAnswer(examQuestionReqDTO.answer());

        return examQuestionMapper.toExamQuestionResDTO(examQuestionRepo.save(examQuestion));
    }

    @Override
    public MessageDTO deleteQuestion(Long questionId) {
        //Verify question exists
        ExamQuestion examQuestion = examQuestionRepo.findById(questionId).orElseThrow(
                () -> new QuestionNotFoundException(questionId));

        //TODO: Verify request has authorization to delete question on this exam

        //Delete question
        examQuestionRepo.delete(examQuestion);

        return new MessageDTO("Question deleted successfully");
    }
}
