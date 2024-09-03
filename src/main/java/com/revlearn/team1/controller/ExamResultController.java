package com.revlearn.team1.controller;

import com.revlearn.team1.dto.course.CourseDTO;
import com.revlearn.team1.model.ExamResult;
import com.revlearn.team1.service.ExamResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public List<ExamResult> findByUserId(@PathVariable int userId)
    {
        return examResultService.findByUserId(userId);
    }
}
