package com.revlearn.team1.service.exam;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.exam.ExamReqDTO;
import com.revlearn.team1.dto.exam.ExamResDTO;
import com.revlearn.team1.dto.examquestion.ExamQuestionResDTO;
import com.revlearn.team1.exceptions.ExamNotFoundException;
import com.revlearn.team1.exceptions.ModuleNotFoundException;
import com.revlearn.team1.mapper.ExamMapper;
import com.revlearn.team1.mapper.ExamQuestionMapper;
import com.revlearn.team1.model.Exam;
import com.revlearn.team1.model.Module;
import com.revlearn.team1.repository.ExamRepo;
import com.revlearn.team1.repository.ModuleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamServiceImp implements ExamService {

    private final ExamRepo examRepo;
    private final ModuleRepo moduleRepo;
    private final ExamMapper examMapper;
    private final ExamQuestionMapper examQuestionMapper;

    @Override
    public ExamResDTO getById(Long examId) {
        //TODO: Verify authenticated user is enrolled student, course owner, or admin account

        return examRepo.findById(examId).map(examMapper::toExamResDTO)
                .orElseThrow(() -> new ExamNotFoundException(examId));
    }

    @Override
    public ExamResDTO createExam(Long moduleId, ExamReqDTO examReqDTO) {
        //TODO Verify authenticated user is course owner or admin account
        //verify that the module exists
        Module module = moduleRepo.findById(moduleId)
                .orElseThrow(() -> new ModuleNotFoundException(moduleId));

        //Create the exam
        Exam exam = examMapper.toExam(examReqDTO);

        //Set the module for the exam
        exam.setModule(module);
        module.getExams().add(exam);

        //Save the exam
        Exam savedExam = examRepo.save(exam);
        moduleRepo.save(module);

        return examMapper.toExamResDTO(savedExam);
    }

    @Override
    public ExamResDTO updateExam(Long examId, ExamReqDTO examReqDTO) {
        //TODO: Verify authenticated user is course owner or admin account

        //Ensure that the exam exists
        Exam retrievedExam = examRepo.findById(examId)
                .orElseThrow(() -> new ExamNotFoundException(examId));

        //TODO: abstract to mapper class
        retrievedExam.setTitle(examReqDTO.title());
        retrievedExam.setDuration(examReqDTO.duration());
        retrievedExam.setInstructions(examReqDTO.instructions());
        retrievedExam.setType(examReqDTO.type());
        retrievedExam.setDescription(examReqDTO.description());

        //TODO Update questions in separate method/route
//        retrievedExam.setQuestions(exam.getQuestions());

        return examMapper.toExamResDTO(examRepo.save(retrievedExam));
    }

    @Override
    public MessageDTO deleteExam(Long examId) {
        //TODO: Verify authenticated user is course owner or admin account

        //Verify that the exam exists
        examRepo.findById(examId)
                .orElseThrow(() -> new ExamNotFoundException(examId));
        //Delete
        examRepo.deleteById(examId);
        return new MessageDTO(String.format("Successfully deleted exam with id: %d", examId));
    }

    @Override
    public List<ExamQuestionResDTO> getQuestionsByExamId(Long examId) {
        //TODO: Verify authenticated user is enrolled student, course owner, or admin account
        // TODO: Consider other restrictions on student access to questions like exam start time, end time, previous attempts, content completion, etc.

        //Verify that the exam exists
        Exam exam = examRepo.findById(examId)
                .orElseThrow(() -> new ExamNotFoundException(examId));

        //Return the questions
        return exam.getQuestions().stream().map(examQuestionMapper::toExamQuestionResDTO)
                .toList();
    }
}
