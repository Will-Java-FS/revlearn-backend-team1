package com.revlearn.team1.service.exam;

import com.revlearn.team1.exceptions.ExamNotFoundException;
import com.revlearn.team1.exceptions.ModuleNotFoundException;
import com.revlearn.team1.exceptions.course.CourseNotFoundException;
import com.revlearn.team1.model.CourseModule;
import com.revlearn.team1.model.Exam;
import com.revlearn.team1.repository.ExamRepo;
import com.revlearn.team1.repository.ModuleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamServiceImp implements ExamService {

    private final ExamRepo examRepo;
    private final ModuleRepo moduleRepo;

    @Override
    public Exam getById(Long examId) {
        //TODO: Verify authenticated user is enrolled student, course owner, or admin account

        return examRepo.findById(examId)
                .orElseThrow(() -> new ExamNotFoundException(examId));
    }

    @Override
    public Exam createExam(Long moduleId, Exam exam) {
        //TODO Verify authenticated user is course owner or admin account
        //verify that the module exists
        CourseModule module = moduleRepo.findById(moduleId)
                .orElseThrow(() -> new ModuleNotFoundException(moduleId));

        //Set the module for the exam
        exam.setCourseModule(module);
        module.getExams().add(exam);

        //Save the exam
        Exam savedExam = examRepo.save(exam);
        moduleRepo.save(module);

        return savedExam;
    }

    @Override
    public Exam updateExam(Long examId, Exam exam) {
        //TODO: Verify authenticated user is course owner or admin account

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
        //TODO: Verify authenticated user is course owner or admin account

        //Verify that the exam exists
        examRepo.findById(examId)
                .orElseThrow(() -> new ExamNotFoundException(examId));
        //Delete
        examRepo.deleteById(examId);
        return String.format("Successfully deleted exam with id: %d", examId);
    }
}
