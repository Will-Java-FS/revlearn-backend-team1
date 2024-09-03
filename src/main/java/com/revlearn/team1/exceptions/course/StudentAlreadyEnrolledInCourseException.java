package com.revlearn.team1.exceptions.course;

public class StudentAlreadyEnrolledInCourseException extends RuntimeException {
    public StudentAlreadyEnrolledInCourseException(String message) {
        super(message);
    }
}
