package com.revlearn.team1.service.exam;

import com.revlearn.team1.exceptions.ExamNotFoundException;
import com.revlearn.team1.model.Exam;
import com.revlearn.team1.repository.ExamRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamServiceImp implements ExamService {

    private final ExamRepo examRepo;

    @Override
    public Exam getById(Long examId) {
        return examRepo.findById(examId)
                .orElseThrow(() -> new ExamNotFoundException(examId));
    }

    @Override
    public Exam createExam(Exam exam) {
        //TODO Verify authenticated user is course owner or admin account

        return examRepo.save(exam);
    }

    @Override
    public Exam updateExam(Long examId, Exam exam) {
        //Ensure that the exam exists
        Exam retrievedExam = examRepo.findById(examId)
                .orElseThrow(() -> new ExamNotFoundException(examId));

        //TODO: abstract to mapper class
        retrievedExam.setTitle(exam.getTitle());
        retrievedExam.setDuration(exam.getDuration());
        retrievedExam.setInstructions(exam.getInstructions());
        retrievedExam.setType(exam.getType());
        retrievedExam.setDescription(exam.getDescription());

        //TODO Update questions in separate method/route
//        retrievedExam.setQuestions(exam.getQuestions());

        return examRepo.save(retrievedExam);
    }

    @Override
    public String deleteExam(Long examId) {
        //Verify that the exam exists
        examRepo.findById(examId)
                .orElseThrow(() -> new ExamNotFoundException(examId));
        //Delete
        examRepo.deleteById(examId);
        return String.format("Successfully deleted exam with id: %d", examId);
    }
}
