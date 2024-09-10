package com.revlearn.team1.controller;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.exam.ExamReqDTO;
import com.revlearn.team1.dto.exam.ExamResDTO;
import com.revlearn.team1.model.Exam;
import com.revlearn.team1.model.ExamQuestion;
import com.revlearn.team1.service.exam.ExamService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamController {

    private final ExamService examService;
    @GetMapping("/{examId}")
    @Operation(summary = "Get Exam by Id", description = "", tags = { "exam" })
    public ExamResDTO getExamById(@PathVariable Long examId) {
        return examService.getById(examId);
    }

    @GetMapping("/{examId}/questions")
    @Operation(summary = "Get Questions of Exam", description = "", tags = { "exam" })
    public List<ExamQuestion> getQuestionsByExamId(@PathVariable Long examId) {
        return examService.getQuestionsByExamId(examId);
    }

    @PostMapping("/module/{moduleId}")
    @Operation(summary = "Create Exam", description = "", tags = { "exam" })
    public ExamResDTO createExam(@PathVariable Long moduleId, @RequestBody ExamReqDTO examReqDTO) {
        return examService.createExam(moduleId, examReqDTO);
    }

    @PutMapping("/{examId}")
    @Operation(summary = "Update Exam", description = "", tags = { "exam" })
    public ExamResDTO updateExam(@PathVariable Long examId, @RequestBody ExamReqDTO examReqDTO) {
        return examService.updateExam(examId, examReqDTO);
    }

    @DeleteMapping("/{examId}")
    @Operation(summary = "Delete Exam", description = "", tags = { "exam" })
    public MessageDTO deleteExam(@PathVariable Long examId) {
        return examService.deleteExam(examId);
    }

}
