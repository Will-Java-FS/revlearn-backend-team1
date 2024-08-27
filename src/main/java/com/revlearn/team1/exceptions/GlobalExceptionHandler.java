package com.revlearn.team1.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CourseNotFoundException.class)
    public ResponseEntity<String> handleCourseNotFoundException(CourseNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceLayerDataAccessException.class)
    public ResponseEntity<Map<String, String>> handleServiceLayerDataAccessException(ServiceLayerDataAccessException ex) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("message", ex.getMessage());
        if (ex.getCause() != null) {
            responseBody.put("cause", ex.getCause().toString());  // Cause of the error
        }
        return new ResponseEntity<>(responseBody, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
