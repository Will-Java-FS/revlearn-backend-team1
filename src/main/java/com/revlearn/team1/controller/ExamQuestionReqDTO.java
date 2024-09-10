package com.revlearn.team1.controller;

import java.util.List;

public record ExamQuestionReqDTO(
    String question,
    List<String> options,
    String answer
) {
}
