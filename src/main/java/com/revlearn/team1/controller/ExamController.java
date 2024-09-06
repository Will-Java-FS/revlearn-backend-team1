package com.revlearn.team1.controller;

import com.revlearn.team1.model.Exam;
import com.revlearn.team1.service.exam.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;
    @GetMapping("/{examId}")
    public Exam getExamById(@PathVariable Long examId) {
        return examService.getById(examId);
    }

    @PostMapping
    public Exam createExam(@RequestBody Exam exam) {
        return examService.createExam(exam);
    }

    @PutMapping("/{examId}")
    public Exam updateExam(@PathVariable Long examId, @RequestBody Exam exam) {
        return examService.updateExam(examId, exam);
    }

    @DeleteMapping("/{examId}")
    public String deleteExam(@PathVariable Long examId) {
        return examService.deleteExam(examId);
    }
}
