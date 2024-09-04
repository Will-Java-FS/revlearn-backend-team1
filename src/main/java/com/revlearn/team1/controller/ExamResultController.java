package com.revlearn.team1.controller;

import com.revlearn.team1.model.ExamResult;
import com.revlearn.team1.service.ExamResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/examresult")
public class ExamResultController {
    @Autowired
    public ExamResultService examResultService;

    public ExamResultController(ExamResultService examResultService)
    {
        this.examResultService = examResultService;
    }

    @GetMapping
    public List<ExamResult> findAllExamResult()
    {
        return examResultService.findAllExamResult();
    }

    @GetMapping("/user/{userId}")
    public List<ExamResult> findByUserId(@PathVariable int userId) {
        return examResultService.findByUserId(userId);
    }

    @GetMapping("/exam/{examId}")
    public List<ExamResult> findByExamId(@PathVariable long examId)
    {
        return examResultService.findByExamId(examId);
    }
}
