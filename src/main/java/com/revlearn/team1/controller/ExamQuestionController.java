package com.revlearn.team1.controller;

import com.revlearn.team1.dto.MessageDTO;
import com.revlearn.team1.dto.examquestion.ExamQuestionResDTO;
import com.revlearn.team1.service.examquestion.ExamQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/question")
public class ExamQuestionController {
    private final ExamQuestionService examQuestionService;

    @GetMapping("/{questionId}")
    public ExamQuestionResDTO getQuestionById(@PathVariable Long questionId) {
        return examQuestionService.getById(questionId);
    }

    @PostMapping("/exam/{examId}")
    public ExamQuestionResDTO createQuestion(@PathVariable Long examId, @RequestBody ExamQuestionReqDTO examQuestionReqDTO) {
        return examQuestionService.createQuestion(examId, examQuestionReqDTO);
    }

    @PutMapping("/{questionId}")
    public ExamQuestionResDTO updateQuestion(@PathVariable Long questionId, @RequestBody ExamQuestionReqDTO examQuestionReqDTO) {
        return examQuestionService.updateQuestion(questionId, examQuestionReqDTO);
    }

    @DeleteMapping("/{questionId}")
    public MessageDTO deleteQuestion(@PathVariable Long questionId) {
        return examQuestionService.deleteQuestion(questionId);
    }
}
