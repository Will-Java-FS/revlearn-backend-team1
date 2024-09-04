package com.revlearn.team1.service;

import com.revlearn.team1.model.ExamResult;
import com.revlearn.team1.model.User;
import com.revlearn.team1.repository.ExamResultRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamResultService {
    @Autowired
    ExamResultRepo examResultRepo;

    public ExamResultService(ExamResultRepo examResultRepo)
    {
        this.examResultRepo = examResultRepo;
    }

    public List<ExamResult> findAllExamResult()
    {
        return examResultRepo.findAll();
    }

    public List<ExamResult> findByUserId(int userId)
    {
        Optional<List<ExamResult>> examResultOptional = Optional.ofNullable(examResultRepo.findByUserId(userId));
        if(examResultOptional.isPresent())
        {
            return examResultOptional.get();
        }
        return null;
    }

    public List<ExamResult> findByExamId(long examId)
    {
        Optional<List<ExamResult>> examResultOptional = Optional.ofNullable(examResultRepo.findByExamId(examId));
        if(examResultOptional.isPresent())
        {
            return examResultOptional.get();
        }
        return null;
    }
}
