package com.revlearn.team1.controller;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.exam.ExamReqDTO;
import com.revlearn.team1.dto.exam.ExamResDTO;
import com.revlearn.team1.model.Exam;
import com.revlearn.team1.model.ExamQuestion;
import com.revlearn.team1.service.exam.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;
    @GetMapping("/{examId}")
    public ExamResDTO getExamById(@PathVariable Long examId) {
        return examService.getById(examId);
    }

    @GetMapping("/{examId}/questions")
    public List<ExamQuestion> getQuestionsByExamId(@PathVariable Long examId) {
        return examService.getQuestionsByExamId(examId);
    }

    @PostMapping("/module/{moduleId}")
    public ExamResDTO createExam(@PathVariable Long moduleId, @RequestBody ExamReqDTO examReqDTO) {
        return examService.createExam(moduleId, examReqDTO);
    }

    @PutMapping("/{examId}")
    public ExamResDTO updateExam(@PathVariable Long examId, @RequestBody ExamReqDTO examReqDTO) {
        return examService.updateExam(examId, examReqDTO);
    }

    @DeleteMapping("/{examId}")
    public MessageDTO deleteExam(@PathVariable Long examId) {
        return examService.deleteExam(examId);
    }
}
