package com.revlearn.team1.exceptions;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(String methodName, Long courseId) {
        super(String.format("%s could not find course by id %d in database.", methodName, courseId));
    }
}
