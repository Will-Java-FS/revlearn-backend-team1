package com.revlearn.team1.exceptions.course;

public class EducatorDoesNotTeachCourseException extends RuntimeException {
    public EducatorDoesNotTeachCourseException(String message) {
        super(message);
    }
}
