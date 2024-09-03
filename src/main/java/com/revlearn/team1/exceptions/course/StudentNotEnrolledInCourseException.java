package com.revlearn.team1.exceptions.course;

public class StudentNotEnrolledInCourseException extends RuntimeException {
    public StudentNotEnrolledInCourseException(String message) {
        super(message);
    }
}
