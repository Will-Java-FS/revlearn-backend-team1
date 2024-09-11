package com.revlearn.team1.exceptions;

public class QuestionNotFoundException extends RuntimeException {
    public QuestionNotFoundException(Long id) {
        super("Question with id " + id + " not found");
    }
}
