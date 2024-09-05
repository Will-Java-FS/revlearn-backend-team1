package com.revlearn.team1.exceptions;

public class ExamNotFoundException extends RuntimeException {
    public ExamNotFoundException(Long examId) {
        super(String.format("Exam with id %d not found", examId));
    }
}
