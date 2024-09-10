package com.revlearn.team1.controller;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.examquestion.ExamQuestionReqDTO;
import com.revlearn.team1.dto.examquestion.ExamQuestionResDTO;
import com.revlearn.team1.service.examquestion.ExamQuestionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/question")
public class ExamQuestionController {
    private final ExamQuestionService examQuestionService;

    @GetMapping("/{questionId}")
    @Operation(summary = "Get Question by Id", description = "", tags = { "question" })
    public ExamQuestionResDTO getQuestionById(@PathVariable Long questionId) {
        return examQuestionService.getById(questionId);
    }

    @PostMapping("/exam/{examId}")
    @Operation(summary = "Create Question", description = "", tags = { "question" })
    public ExamQuestionResDTO createQuestion(@PathVariable Long examId, @RequestBody ExamQuestionReqDTO examQuestionReqDTO) {
        return examQuestionService.createQuestion(examId, examQuestionReqDTO);
    }

    @PutMapping("/{questionId}")
    @Operation(summary = "Update Question", description = "", tags = { "question" })
    public ExamQuestionResDTO updateQuestion(@PathVariable Long questionId, @RequestBody ExamQuestionReqDTO examQuestionReqDTO) {
        return examQuestionService.updateQuestion(questionId, examQuestionReqDTO);
    }

    @DeleteMapping("/{questionId}")
    @Operation(summary = "Delete Question", description = "", tags = { "question" })
    public MessageDTO deleteQuestion(@PathVariable Long questionId) {
        return examQuestionService.deleteQuestion(questionId);
    }
}
